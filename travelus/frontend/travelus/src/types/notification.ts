export interface NotificationData {
  targetUserId: number;
  title: string;
  message: string;
  icon: string;
}

export interface NotificationListInfo {
  notificationId: number;
  userId: number;
  title: string;
  message: string;
  notificationType: string;
  accountNo: string;
  createdAt: string;
  read: boolean;
  settlementId?: string;
  groupId?: number;
  currencyCode?: string;
}