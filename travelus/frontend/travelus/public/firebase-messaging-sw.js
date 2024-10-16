// const baseUrl = "http://localhost:3000"; // 로컬
const baseUrl = "https://j11d209.p.ssafy.io"; // 서비스

self.addEventListener("install", function (e) {
  console.log("fcm sw install..");
  self.skipWaiting(); // 새로운 서비스 워커가 대기하지 않고 바로 활성화되도록
});

self.addEventListener("activate", function (e) {
  console.log("fcm sw activate..");
  e.waitUntil(
    self.clients.claim() // 활성화되자마자 클라이언트를 제어할 수 있도록
  );
});

self.addEventListener("push", function (e) {
  if (!e.data.json()) return;

  console.log("e.date.json : ", e.data.json());

  const resultData = e.data.json().notification;
  const notificationTitle = resultData.title;
  const notificationOptions = {
    body: resultData.body,
    icon: resultData.icon,
    tag: resultData.tag,
    data: e.data.json().data,
  };
  console.log("service-worker 백그라운드 수신: ", { resultData, notificationTitle, notificationOptions });

  self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener("notificationclick", function (event) {
  console.log("notification click");

  event.notification.close();

  const data = event.notification.data;
  const notificationType = data?.notificationType;
  const accountNo = data?.accountNo;
  const groupId = data?.groupId;
  const currencyCode = data?.currencyCode;
  const settlementId = data?.settlementId;

  const urlToOpen = generateUrl(notificationType, accountNo, groupId, currencyCode, settlementId);

  event.waitUntil(
    (async () => {
      const allClients = await clients.matchAll({
        type: "window",
        includeUncontrolled: true
      });

      console.log("notificationType : ", notificationType);
      console.log("openToUrl : ", urlToOpen);


      let rootClient = allClients.find(client => client.url.includes("/") && 'focus' in client);

      if (rootClient) {
        try {
          // 페이지에 메시지를 보내서 URL로 이동시키도록 요청
          rootClient.postMessage({ action: 'navigate', url: urlToOpen });
          return rootClient.focus(); // 포커스는 그대로
        } catch (e) {
          console.log("포커스할 수 없습니다: ", e);
        }
      }

      return clients.openWindow(urlToOpen).then(windowClient => windowClient ? windowClient.focus() : null);
    })()
  );
});

// URL 생성 헬퍼 함수
const generateUrl = (type, accountNo, groupId, currencyCode, settlementId) => {
  if (!accountNo) {
    console.log("Error : 계좌 정보가 없습니다.");
    return "/";
  }

  switch (type) {
    case "PT":
      return `${baseUrl}/transaction/${accountNo}`;
    case "GT":
      return `${baseUrl}/meetingtransaction/${accountNo}/notification`;
    case "E":
      return `${baseUrl}/travelbox/transaction/${accountNo}/notification?groupId=${groupId}&currencyCode=${currencyCode}`;
    case "S":
      return `${baseUrl}/settlement/expenditure/list/NOT_COMPLETED`;
    case "GD":
      return `${baseUrl}/meetingtransaction/${accountNo}/detail`;
    default:
      return "/";
  }
};