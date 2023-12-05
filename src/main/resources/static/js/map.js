navigator.geolocation.getCurrentPosition(success, error);

async function success(position) {
  const lat = position.coords.latitude;
  const lng = position.coords.longitude;
  let latlng = new naver.maps.LatLng(lat, lng);
  let infoWindowTemp = new naver.maps.InfoWindow();
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
  naver.maps.Event.addListener(map, 'keyup', () => {
    map.panTo(latlng);
  });
  // 클릭한 지점으로 원(도형),반경 이동
  naver.maps.Event.addListener(map, 'click', (event) => {
    latlng = event.coord;

    infoWindowTemp.close();
    circle.setCenter(latlng);
    radius.setCenter(latlng);
  });
  // 로고, 게시판, 위치 버튼 추가
  naver.maps.Event.once(map, 'init', () => {
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
    const boardButtonHtml = `<a href="/board/list">
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
    const bounds = map.getBounds();

    locationButtonControl.setMap(map);
    logoButtonControl.setMap(map);
    boardButtonControl.setMap(map);

    naver.maps.Event.addDOMListener(
      locationButtonControl.getElement(),
      'click',
      () => {
        latlng = new naver.maps.LatLng(lat, lng);

        infoWindowTemp.close();
        circle.setCenter(latlng);
        radius.setCenter(latlng);
        map.panToBounds(bounds);
      }
    );
  });

  // 행정처분결과(식품접객업) 반환 받는 API
  const response = await fetch('/api/restaurants');
  const data = await response.json();
  const restaurants = await data.filter((r1, idx1) => {
    const name1 = r1.prcscitypointBsshnm;
    const lat1 = r1.lat;
    const lng1 = r1.lng;
    const idx2 = data.findIndex((r2) => {
      const name2 = r2.prcscitypointBsshnm;
      const lat2 = r2.lat;
      const lng2 = r2.lng;

      return name1 === name2 && lat1 === lat2 && lng1 === lng2;
    });

    return idx1 === idx2;
  });

  // 마커 생성
  const markers = [];
  const inforWindows = [];

  for (const restaurant of restaurants) {
    const name = restaurant.prcscitypointBsshnm;
    const addr = restaurant.addr;
    const lat = restaurant.lat;
    const lng = restaurant.lng;
    const viltcns = data.filter((r) => r.prcscitypointBsshnm === name);
    let liHtml = '';

    for (const viltcn of viltcns) {
      liHtml += `<li>${viltcn.viltcn}</li>`;
    }

    if (lat !== null && lng !== null) {
      const marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(lat, lng),
        title: name,
        icon: {
          url: '/images/marker.png',
          size: new naver.maps.Size(25, 34),
          scaledSize: new naver.maps.Size(25, 34),
          origin: new naver.maps.Point(0, 0),
          anchor: new naver.maps.Point(12, 34),
        },
      });
      const infoWindow = new naver.maps.InfoWindow({
        content: `<div class="p-2">
                    <p class="font-bold text-lg">${name}</p>
                    <p class="text-xs">${addr}</p>
                    <div class="py-2">
                      <p class="font-bold">위반일자 및 위반내용</p>
                      <ul class="list-disc px-5">
                        ${liHtml}
                      </ul>
                    </div>
                  </div>`,
        maxWidth: 320,
      });

      markers.push(marker);
      inforWindows.push(infoWindow);

      naver.maps.Event.addListener(marker, 'click', () => {
        infoWindowTemp = infoWindow;

        if (infoWindow.getMap()) {
          infoWindow.close();
        } else {
          infoWindow.open(map, marker);
        }
      });
    }
  }

  // 클러스터링
  const htmlMarker1 = {
    content:
      '<button type="button" class="text-white bg-red-300 hover:bg-red-400 font-medium rounded-full text-sm w-10 h-10 text-center"></button>',
  };
  const htmlMarker2 = {
    content:
      '<button type="button" class="text-white bg-red-400 hover:bg-red-500 font-medium rounded-full text-sm w-10 h-10 text-center"></button>',
  };
  const htmlMarker3 = {
    content:
      '<button type="button" class="text-white bg-red-500 hover:bg-red-600 font-medium rounded-full text-sm w-10 h-10 text-center"></button>',
  };
  const htmlMarker4 = {
    content:
      '<button type="button" class="text-white bg-red-600 hover:bg-red-700 font-medium rounded-full text-sm w-10 h-10 text-center"></button>',
  };
  const htmlMarker5 = {
    content:
      '<button type="button" class="text-white bg-red-700 hover:bg-red-800 font-medium rounded-full text-sm w-10 h-10 text-center"></button>',
  };
  const markerClustering = new MarkerClustering({
    minClusterSize: 2,
    maxZoom: 13,
    map: map,
    markers: markers,
    disableClickZoom: false,
    gridSize: 120,
    icons: [htmlMarker1, htmlMarker2, htmlMarker3, htmlMarker4, htmlMarker5],
    indexGenerator: [10, 100, 200, 500, 1000],
    stylingFunction: (clusterMarker, count) => {
      clusterMarker.getElement().querySelector('button:first-child').innerText =
        count;
    },
  });

  // 로딩 종료
  const backdrop = document.querySelector('#backdrop');

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
