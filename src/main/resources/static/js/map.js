navigator.geolocation.getCurrentPosition(success, error);

async function success(position) {
  const lat = position.coords.latitude;
  const lng = position.coords.longitude;
  let latlng = new naver.maps.LatLng(lat, lng);

  // 지도 생성
  const mapOptions = {
    center: latlng,
    zoom: 14,
  };
  const map = new naver.maps.Map('map', mapOptions);

  // 원(도형) 생성
  const circleOptions = {
    center: latlng,
    map: map,
    radius: 25,
    fillColor: 'crimson',
    fillOpacity: 0.8,
  };
  const circle = new naver.maps.Circle(circleOptions);
  // 반경 생성
  const radiusOptions = {
    center: latlng,
    map: map,
    radius: 1000,
    fillColor: 'crimson',
    fillOpacity: 0.3,
  };
  const radius = new naver.maps.Circle(radiusOptions);
  // 새로운 위치로 원(도형),반경 이동
  naver.maps.Event.addListener(map, 'keydown', (event) => {
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
  // 클릭한 지점으로 원(도형),반경 이동
  naver.maps.Event.addListener(map, 'click', function (e) {
    latlng = e.coord;
    circle.setCenter(latlng);
    radius.setCenter(latlng);
  });

  // 지정한 위치로 지도를 이동
  naver.maps.Event.addListener(map, 'keyup', () => {
    map.panTo(latlng);
  });
  // 위치 버튼 클릭 시 처음 위치로 이동
  naver.maps.Event.once(map, 'init', function () {
    // 위치 버튼 생성
    const locationBtnHtml = `<button type="button" class="p-1 bg-white hover:bg-slate-200 mr-2 ">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="red" class="w-6 h-6">
      <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" />
      <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" />
      </svg>
    </button>`;
    const customControl = new naver.maps.CustomControl(locationBtnHtml, {
      position: naver.maps.Position.RIGHT_CENTER,
    });
    customControl.setMap(map);

    // 처음 위치로 이동
    naver.maps.Event.addDOMListener(customControl.getElement(), 'click', () => {
      latlng = new naver.maps.LatLng(lat, lng);
      circle.setCenter(latlng);
      radius.setCenter(latlng);
      map.setCenter(new naver.maps.LatLng(lat, lng));
    });
  });

  let response = await fetch('/api/restaurant/show');
  const data = await response.json();
  console.log(data);
  // data.forEach(async (restaurant) => {
  // const addr = restaurant.addr;
  // const response = await fetch(
  //   `https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=${addr}`,
  //   {
  //     method: 'GET',
  //     headers: {
  //       'X-NCP-APIGW-API-KEY-ID': 'dxkas2z55y',
  //       'X-NCP-APIGW-API-KEY': 'wcNZKPlRmcDFMKOgAM0n1dxYRYJZwBeSMfRz24O5',
  //     },
  //   }
  // );
  // const data = await response.json();
  // });
}

function error() {
  alert('Sorry, no position available.');
}

function calculateCoordinates(lat, lng, distance, direction) {
  const earthRadius = 6371;
  const distanceInRadians = distance / earthRadius;

  let newLat, newLng;

  switch (direction) {
    case 'ArrowUp':
      newLat = lat + distanceInRadians * (180 / Math.PI);
      newLng = lng;
      break;
    case 'ArrowDown':
      newLat = lat - distanceInRadians * (180 / Math.PI);
      newLng = lng;
      break;
    case 'ArrowLeft':
      newLat = lat;
      newLng =
        lng -
        (distanceInRadians * (180 / Math.PI)) / Math.cos((lat * Math.PI) / 180);
      break;
    case 'ArrowRight':
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
