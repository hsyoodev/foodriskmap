navigator.geolocation.getCurrentPosition(success, error);

function success(position) {
  let latitude = position.coords.latitude;
  let longitude = position.coords.longitude;
  let latlng = new naver.maps.LatLng(latitude, longitude);

  // 지도
  const mapOptions = {
    center: latlng,
    zoom: 15,
  };
  const map = new naver.maps.Map("map", mapOptions);

  // 위치
  const circleOptions = {
    center: latlng,
    map: map,
    radius: 25,
    fillColor: "crimson",
    fillOpacity: 0.8,
  };
  const circle = new naver.maps.Circle(circleOptions);
  // 반경
  const radiusOptions = {
    center: latlng,
    map: map,
    radius: 1000,
    fillColor: "crimson",
    fillOpacity: 0.3,
  };
  const radius = new naver.maps.Circle(radiusOptions);

  var locationBtnHtml = `<svg
    xmlns="http://www.w3.org/2000/svg"
    fill="none"
    viewBox="0 0 24 24"
    stroke-width="1.5"
    stroke="currentColor"
    class="w-6 h-6 m-4"
  >
    <path
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M12 9v3.75m0-10.036A11.959 11.959 0 013.598 6 11.99 11.99 0 003 9.75c0 5.592 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.57-.598-3.75h-.152c-3.196 0-6.1-1.249-8.25-3.286zm0 13.036h.008v.008H12v-.008z"
    />
  </svg>`;

  naver.maps.Event.once(map, "init", function () {
    var customControl = new naver.maps.CustomControl(locationBtnHtml, {
      position: naver.maps.Position.RIGHT_CENTER,
    });

    customControl.setMap(map);

    naver.maps.Event.addDOMListener(
      customControl.getElement(),
      "click",
      function () {
        map.setCenter(new naver.maps.LatLng(latitude, longitude));
      }
    );
  });

  // 상/하/좌/우 keydown 시 위치,반경 이동
  naver.maps.Event.addListener(map, "keydown", (event) => {
    const key = event.keyboardEvent.key;
    const { lat: newLat, lon: newLon } = calculateCoordinates(
      latlng._lat,
      latlng._lng,
      1,
      key
    );

    latlng = new naver.maps.LatLng(newLat, newLon);

    circle.setCenter(latlng);
    radius.setCenter(latlng);
  });

  // keyup 시 변경된 위치로 이동
  naver.maps.Event.addListener(map, "keyup", (event) => {
    map.panTo(latlng);
  });
}

function error() {
  alert("Sorry, no position available.");
}

function calculateCoordinates(lat, lng, distance, direction) {
  const earthRadius = 6371;
  const distanceInRadians = distance / earthRadius;

  let newLat, newLng;

  switch (direction) {
    case "ArrowUp":
      newLat = lat + distanceInRadians * (180 / Math.PI);
      newLng = lng;
      break;
    case "ArrowDown":
      newLat = lat - distanceInRadians * (180 / Math.PI);
      newLng = lng;
      break;
    case "ArrowLeft":
      newLat = lat;
      newLng =
        lng -
        (distanceInRadians * (180 / Math.PI)) / Math.cos((lat * Math.PI) / 180);
      break;
    case "ArrowRight":
      newLat = lat;
      newLng =
        lng +
        (distanceInRadians * (180 / Math.PI)) / Math.cos((lat * Math.PI) / 180);
      break;
    default:
      newLat = lat;
      newLng = lng;
  }

  return { lat: newLat, lon: newLng };
}
