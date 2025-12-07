import { request } from './request';
import { ShopItem } from './data';
import { Category, GoodsItem, mockCanteenDetail, mockCanteenList, mockCreateOrder, mockOrders } from './mock';

const USE_MOCK = false;

const unwrap = <T = any>(resp: any): T => {
  if (resp && typeof resp === 'object' && 'data' in resp) {
    return (resp as any).data as T;
  }
  return resp as T;
};

export interface CanteenDetailResponse {
  shopInfo: ShopItem;
  categories: Category[];
  comments: any[];
}

export const loginWithCode = async (code: string) => {
  if (USE_MOCK) {
    return {
      token: 'mock-token',
      user: {
        id: 1,
        nickname: '演示用户'
      }
    };
  }
  const resp = await request({
    url: '/auth/wechat/login',
    method: 'POST',
    data: { code }
  });
  return unwrap(resp);
};

export const fetchHomeFeed = async () => {
  if (USE_MOCK) {
    return {
      banners: [],
      announcements: [],
      recommendCanteens: mockCanteenList({ page: 1, pageSize: 10 }).list
    };
  }
  const resp = await request({
    url: '/home/feed',
    method: 'GET'
  });
  return unwrap(resp);
};

export const fetchCanteens = async (params: { page: number; pageSize: number; keyword?: string; categoryId?: number; sort?: string }) => {
  if (USE_MOCK) {
    return mockCanteenList(params);
  }
  const resp = await request({
    url: '/canteens',
    method: 'GET',
    data: {
      page: params.page,
      pageSize: params.pageSize,
      keyword: params.keyword,
      categoryId: params.categoryId,
      sort: params.sort
    }
  });
  return unwrap(resp);
};

export const fetchCanteenDetail = async (canteenId: number): Promise<CanteenDetailResponse> => {
  if (USE_MOCK) {
    return mockCanteenDetail(canteenId);
  }
  const [shopInfo, categories, comments] = await Promise.all([
    request({
      url: `/canteens/${canteenId}`,
      method: 'GET'
    }),
    request({
      url: `/canteens/${canteenId}/dishes`,
      method: 'GET'
    }),
    request({
      url: `/shops/${canteenId}/comments`,
      method: 'GET'
    })
  ]);
  return {
    shopInfo: unwrap<ShopItem>(shopInfo),
    categories: unwrap<Category[]>(categories),
    comments: unwrap<any[]>(comments)
  };
};

export const fetchOrders = async (params: { status: string; page: number; pageSize: number }) => {
  if (USE_MOCK) {
    return mockOrders(params);
  }
  const resp = await request({
    url: '/orders',
    method: 'GET',
    data: params
  });
  return unwrap(resp);
};

export const cancelOrder = async (orderId: number) => {
  if (USE_MOCK) {
    return { success: true };
  }
  const resp = await request({
    url: `/orders/${orderId}/cancel`,
    method: 'POST'
  });
  return unwrap(resp);
};

export const payOrder = async (orderId: number) => {
  if (USE_MOCK) {
    return { success: true };
  }
  const resp = await request({
    url: `/orders/${orderId}/pay`,
    method: 'POST'
  });
  return unwrap(resp);
};

export const confirmPickup = async (orderId: number) => {
  if (USE_MOCK) {
    return { success: true };
  }
  const resp = await request({
    url: `/merchant/orders/${orderId}/complete`,
    method: 'POST'
  });
  return unwrap(resp);
};

export const createOrder = async (payload: {
  shopId: number;
  cartList: GoodsItem[];
  totalPrice: number;
  diningMode: string;
  tableNo?: string;
  pickupTime?: string;
  remark?: string;
}) => {
  if (USE_MOCK) {
    return mockCreateOrder();
  }
  const resp = await request({
    url: '/orders',
    method: 'POST',
    data: payload
  });
  return unwrap(resp);
};
