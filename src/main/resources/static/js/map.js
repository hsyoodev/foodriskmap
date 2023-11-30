navigator.geolocation.getCurrentPosition(success, error);

async function success(position) {
  const lat = position.coords.latitude;
  const lng = position.coords.longitude;
  let latlng = new naver.maps.LatLng(lat, lng);
  const map = new naver.maps.Map('map', {
    center: latlng,
    zoom: 14,
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

  // 새로운 위치로 원(도형),반경 이동
  naver.maps.Event.addListener(map, 'keydown', (event) => {
    const key = event.keyboardEvent.key;
    const km = 1;
    const { lat: newLat, lon: newLon } = calculateCoordinates(
      latlng._lat,
      latlng._lng,
      km,
      key
    );

    latlng = new naver.maps.LatLng(newLat, newLon);
    circle.setCenter(latlng);
    radius.setCenter(latlng);
  });
  // 클릭한 지점으로 원(도형),반경 이동
  naver.maps.Event.addListener(map, 'click', (event) => {
    latlng = event.coord;

    circle.setCenter(latlng);
    radius.setCenter(latlng);
  });
  // 이동한 위치로 지도를 부드럽게 이동
  naver.maps.Event.addListener(map, 'keyup', () => {
    map.panTo(latlng);
  });
  // 버튼 추가
  naver.maps.Event.once(map, 'init', () => {
    const bounds = map.getBounds();
    const locationButtonHtml = `<button type="button" class="p-1 bg-rose-500 hover:bg-rose-600 mr-2">
                              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="white" class="w-6 h-6">
                              <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" />
                              <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" />
                              </svg>
                            </button>`;
    const logoButtonHtml = `<a href="/">
                              <div class="flex space-x-2 rounded-full text-white bg-rose-600 hover:bg-rose-700 hover:cursor-pointer font-bold m-2 p-2">
                                <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  fill="none"
                                  viewBox="0 0 24 24"
                                  stroke-width="1.5"
                                  stroke="currentColor"
                                  class="w-6 h-6"
                                >
                                  <path
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    d="M12 9v3.75m0-10.036A11.959 11.959 0 013.598 6 11.99 11.99 0 003 9.75c0 5.592 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.57-.598-3.75h-.152c-3.196 0-6.1-1.249-8.25-3.286zm0 13.036h.008v.008H12v-.008z"
                                  />
                                </svg>
                                <span>FoodRiskMap</span>
                              </div>
                            </a>`;
    const boardButtonHtml = `<a href="/board">
                              <div class="flex space-x-2 rounded-full text-white bg-rose-600 hover:bg-rose-700 hover:cursor-pointer font-bold m-2 p-2">
                                <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke-width="1.5"
                                stroke="currentColor"
                                class="w-6 h-6"
                              >
                                  <path
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    d="M15.666 3.888A2.25 2.25 0 0013.5 2.25h-3c-1.03 0-1.9.693-2.166 1.638m7.332 0c.055.194.084.4.084.612v0a.75.75 0 01-.75.75H9a.75.75 0 01-.75-.75v0c0-.212.03-.418.084-.612m7.332 0c.646.049 1.288.11 1.927.184 1.1.128 1.907 1.077 1.907 2.185V19.5a2.25 2.25 0 01-2.25 2.25H6.75A2.25 2.25 0 014.5 19.5V6.257c0-1.108.806-2.057 1.907-2.185a48.208 48.208 0 011.927-.184"
                                  />
                                </svg>
                                <span>Board</span>
                              </div>
                            </a>`;
    const locationButtonControl = new naver.maps.CustomControl(
      locationButtonHtml,
      {
        position: naver.maps.Position.RIGHT_CENTER,
      }
    );
    const logoButtonControl = new naver.maps.CustomControl(logoButtonHtml, {
      position: naver.maps.Position.TOP_LEFT,
    });
    const boardButtonControl = new naver.maps.CustomControl(boardButtonHtml, {
      position: naver.maps.Position.TOP_RIGHT,
    });

    locationButtonControl.setMap(map);
    logoButtonControl.setMap(map);
    boardButtonControl.setMap(map);
    naver.maps.Event.addDOMListener(
      locationButtonControl.getElement(),
      'click',
      () => {
        latlng = new naver.maps.LatLng(lat, lng);

        circle.setCenter(latlng);
        radius.setCenter(latlng);
        map.panToBounds(bounds);
      }
    );
  });
  // 지도가 움직이면
  // 보이는 영역은 마커 생성
  // 벗어난 영역은 마커 숨김
  naver.maps.Event.addListener(map, 'idle', async () => {
    updateMarkers(map, markers);
  });

  // 행정처분결과(식품접객업) 반환 받는 API
  const response = await fetch('/api/restaurant/show');
  const restaurants = await response.json();
  const backdrop = document.querySelector('#backdrop');
  const markers = [];

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
  backdrop.remove();
}

function error() {
  alert('위치를 찾을 수 없습니다.');
}

function calculateCoordinates(lat, lng, distance, direction) {
  const earthRadius = 6371;
  const distanceInRadians = distance / earthRadius;
  let newLat = lat;
  let newLng = lng;

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
  }

  return { lat: newLat, lon: newLng };
}

function updateMarkers(map, markers) {
  const bounds = map.getBounds();

  for (const marker of markers) {
    const position = marker.getPosition();

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
