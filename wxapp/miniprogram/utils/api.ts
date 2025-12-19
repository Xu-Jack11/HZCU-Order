import { request } from './request';
import { ShopItem } from './data';
import { Category, GoodsItem, mockCanteenDetail, mockCanteenList, mockCreateOrder, mockOrders } from './mock';

const USE_MOCK = false;

const unwrap = <T = any>(resp: any): T => {
  if (resp && typeof resp === 'object' && 'success' in resp) {
    return resp.data as T;
  }
  return resp as T;
};

export interface CanteenDetailResponse {
  shopInfo: ShopItem;
  categories: Category[];
  comments: any[];
}

export const loginWithCode = async (code: string, nickname?: string, avatarUrl?: string) => {
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
    url: '/auth/login/wechat',
    method: 'POST',
    data: { code, nickname, avatarUrl }
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
    url: '/canteens',
    method: 'GET'
  });
  const canteens = unwrap(resp);
  return {
    banners: [],
    announcements: [],
    recommendCanteens: canteens.map((c: any) => ({
      id: c.canteenId,
      name: c.name,
      logo: c.imageUrl,
      rating: 4.8,
      monthlySales: 100,
      waitTime: 15,
      distance: '0.5km',
      minPrice: 0,
      tags: [c.campus],
      categoryIds: []
    }))
  };
};

export const fetchCanteens = async (params: { page: number; pageSize: number; keyword?: string; categoryId?: number; sort?: string }) => {
  if (USE_MOCK) {
    return mockCanteenList(params);
  }
  const resp = await request({
    url: '/canteens',
    method: 'GET',
    data: params
  });
  const list = unwrap(resp);
  return {
    list: list.map((c: any) => ({
      id: c.canteenId,
      name: c.name,
      logo: c.imageUrl,
      rating: 4.8,
      monthlySales: 100,
      waitTime: 15,
      distance: '0.5km',
      minPrice: 0,
      tags: [c.campus],
      categoryIds: []
    })),
    total: list.length
  };
};

export const fetchCanteenDetail = async (canteenId: number): Promise<CanteenDetailResponse> => {
  if (USE_MOCK) {
    return mockCanteenDetail(canteenId);
  }
  const [shopInfoResp, categoriesResp] = await Promise.all([
    request({
      url: `/canteens/${canteenId}`,
      method: 'GET'
    }),
    request({
      url: `/canteens/${canteenId}/categories`,
      method: 'GET'
    })
  ]);

  const shopInfo = unwrap(shopInfoResp);
  const categoriesData = unwrap<any[]>(categoriesResp);

  const categories: Category[] = categoriesData.map(cat => ({
    id: cat.categoryId,
    name: cat.name,
    goods: cat.dishes ? cat.dishes.map((d: any) => ({
      id: d.dishId,
      name: d.name,
      description: d.description || '',
      image: d.imageUrl || '',
      price: d.price,
      monthlySales: d.sales || 0,
      goodRate: 100,
      tags: [],
      count: 0
    })) : []
  }));

  return {
    shopInfo: {
      id: shopInfo.canteenId,
      name: shopInfo.name,
      logo: shopInfo.imageUrl,
      rating: 4.8,
      monthlySales: 100,
      waitTime: 15,
      distance: '0.5km',
      minPrice: 0,
      tags: [shopInfo.campus],
      categoryIds: [],
      notice: shopInfo.description
    },
    categories: categories,
    comments: []
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

  const rawOrders = unwrap<any[]>(resp);
  const mappedOrders = rawOrders.map(o => ({
    id: o.orderId,
    shopId: o.canteenId,
    shopName: o.canteenName,
    shopLogo: o.canteenLogo,
    goods: o.items ? o.items.map((it: any) => ({
      id: it.dishId,
      name: it.dishName,
      image: it.dishImage,
      price: it.price,
      count: it.quantity
    })) : [],
    totalCount: o.items ? o.items.reduce((acc: number, cur: any) => acc + cur.quantity, 0) : 0,
    totalPrice: o.totalAmount,
    status: o.status.toLowerCase(),
    statusText: o.status,
    createTime: o.createdAt
  }));

  return {
    list: mappedOrders,
    total: mappedOrders.length
  };
};

export const createOrder = async (payload: {
  shopId: number;
  cartList: any[];
  totalPrice: number;
  diningMode: string;
  tableNo?: string;
  pickupTime?: string;
  remark?: string;
}) => {
  if (USE_MOCK) {
    return mockCreateOrder();
  }

  const orderData = {
    canteenId: payload.shopId,
    items: payload.cartList.map(item => ({
      dishId: item.id,
      quantity: item.count,
      price: item.price,
      dishName: item.name
    })),
    totalAmount: payload.totalPrice,
    diningMode: payload.diningMode,
    remark: payload.remark
  };

  const resp = await request({
    url: '/orders',
    method: 'POST',
    data: orderData
  });
  return unwrap(resp);
};

export const payOrder = async (orderId: number) => {
  if (USE_MOCK) return { success: true };
  const resp = await request({
    url: `/payment/create/${orderId}?channel=WECHAT`,
    method: 'POST'
  });
  return unwrap(resp);
};
