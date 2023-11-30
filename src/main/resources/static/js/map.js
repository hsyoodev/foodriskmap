navigator.geolocation.getCurrentPosition(success, error);

async function success(position) {
  const lat = position.coords.latitude;
  const lng = position.coords.longitude;
  let latlng = new naver.maps.LatLng(lat, lng);
  const map = new naver.maps.Map('map', {
    center: latlng,
    zoom: 14,
    zIndex: 10,
  });
  const circle = new naver.maps.Circle({
    center: latlng,
    map: map,
    radius: 25,
    fillColor: 'crimson',
    fillOpacity: 0.8,
  });
  const radius = new naver.maps.Circle({
    center: latlng,
    map: map,
    radius: 1000,
    fillColor: 'crimson',
    fillOpacity: 0.3,
  });
  const bounds = map.getBounds();
  const southWest = bounds.getSW();
  const northEast = bounds.getNE();
  const markers = [];
  const backdrop = document.querySelector('#backdrop');
  // 행정처분결과(식품접객업) 반환 받는 API
  const response = await fetch('/api/restaurant/show');
  const restaurants = await response.json();

  // 마커 생성
  for (const restaurant of restaurants) {
    const name = restaurant.prcscitypointBsshnm;
    const lat = restaurant.lat;
    const lng = restaurant.lng;

    if (lat !== null && lng !== null) {
      const marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(lat, lng),
        title: name,
      });
      markers.push(marker);
    }
  }

  // 지도에 마커 표시
  updateMarkers(map, markers);
  // 로딩 종료
  backdrop.classList.add('hidden');

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
  // 이동한 위치로 지도를 부드럽게 이동
  naver.maps.Event.addListener(map, 'keyup', () => {
    map.panTo(latlng);
  });
  // 처음 위치로 부드럽게 이동
  naver.maps.Event.once(map, 'init', function () {
    const locationBtnHtml = `<button type="button" class="p-1 bg-rose-500 hover:bg-rose-600 mr-2 z-auto">
                              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="white" class="w-6 h-6">
                              <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" />
                              <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" />
                              </svg>
                            </button>`;
    const customControl = new naver.maps.CustomControl(locationBtnHtml, {
      position: naver.maps.Position.RIGHT_CENTER,
    });

    customControl.setMap(map);
    naver.maps.Event.addDOMListener(customControl.getElement(), 'click', () => {
      const latlngBounds = new naver.maps.LatLngBounds(southWest, northEast);
      latlng = new naver.maps.LatLng(lat, lng);

      circle.setCenter(latlng);
      radius.setCenter(latlng);
      map.panToBounds(latlngBounds);
    });
  });
  // 지도가 움직이면
  // 보이는 영역에 마커 생성
  // 벗어난 영역은 마커 숨김
  naver.maps.Event.addListener(map, 'idle', async () => {
    updateMarkers(map, markers);
  });
}

function error() {
  alert('위치를 찾을 수 없습니다.');
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

function updateMarkers(map, markers) {
  const bounds = map.getBounds();

  for (const marker of markers) {
    let position = marker.getPosition();

    if (bounds.hasLatLng(position)) {
      showMarker(map, marker);
      continue;
    }
    hideMarker(marker);
  }
}

function showMarker(map, marker) {
  marker.setMap(map);
}

function hideMarker(marker) {
  marker.setMap(null);
}
