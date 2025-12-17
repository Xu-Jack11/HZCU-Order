export type Canteen = {
  id: string;
  name: string;
  address?: string;
  manager?: string;
  phone?: string;
  rating?: number;
  status?: 'OPEN' | 'CLOSED';
};

export type Shop = {
  id: number | string;
  name: string;
  logo?: string;
  rating?: number;
  monthlySales?: number;
};

export type OrderItem = {
  name: string;
  quantity: number;
  spec?: string;
};

export type OrderStatus = 'PENDING' | 'PROCESSING' | 'READY' | 'COMPLETED' | 'CANCELLED';

export type Order = {
  id: string;
  number: string;
  createTime: string;
  status: OrderStatus;
  items: OrderItem[];
  totalAmount: number;
  note?: string;
};

export type PageResult<T> = { list: T[]; total: number };

// 默认走同源 '/api'，由 next.config.mjs 的 rewrites 代理到后端的 '/api/v1'，避免跨域问题。
// 若需要自定义后端地址，可将 NEXT_PUBLIC_API_BASE 设置为完整前缀，例如 'http://localhost:8080/api/v1'
const API_BASE = process.env.NEXT_PUBLIC_API_BASE || '/api';

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers || {}),
    },
    cache: 'no-store',
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`API ${path} ${res.status}: ${text}`);
  }
  const json = await res.json();
  // 后端统一返回 { code, message, data }
  if (json && typeof json === 'object' && 'data' in json) {
    return json.data as T;
  }
  return json as T;
}

export const api = {
  getCanteens: () => request<Canteen[]>('/canteens'),
  getOrders: (params?: { status?: OrderStatus; page?: number; pageSize?: number }) => {
    const qs: string[] = [];
    if (params?.status) qs.push(`status=${params.status}`);
    if (params?.page) qs.push(`page=${params.page}`);
    if (params?.pageSize) qs.push(`pageSize=${params.pageSize}`);
    const q = qs.length ? `?${qs.join('&')}` : '';
    return request<PageResult<Order>>(`/orders${q}`);
  },
  updateOrderStatus: async (id: string, status: OrderStatus) => {
    // 后端已有接口：pay、cancel；完成用 complete（需要后端补充或改造）。
    const lid = id;
    if (status === 'PROCESSING') {
      return request<Order>(`/orders/${lid}/pay`, { method: 'POST' });
    }
    if (status === 'READY') {
      // 商家端叫号取餐使用 merchant 路由
      return request<Order>(`/merchant/orders/${lid}/ready`, { method: 'POST' });
    }
    if (status === 'CANCELLED') {
      return request<Order>(`/orders/${lid}/cancel`, { method: 'POST' });
    }
    if (status === 'COMPLETED') {
      return request<Order>(`/merchant/orders/${lid}/complete`, { method: 'POST' });
    }
    // 其他状态暂不支持服务器侧变更
    throw new Error('不支持的状态更新');
  },
  // Menu related
  listCanteensPaged: (page = 1, pageSize = 1000) =>
    request<PageResult<Canteen>>(`/canteens?page=${page}&pageSize=${pageSize}`),
  getShopDishes: (shopId: string | number, page = 1, pageSize = 10000) =>
    request<Array<{ id: number; name: string; goods: Array<{ id: number; name: string; description?: string; image?: string; price: number; monthlySales?: number; goodRate?: number }> }>>(
      `/canteens/${shopId}/dishes?page=${page}&pageSize=${pageSize}`
    ),
  updateDish: (id: string | number, payload: { name?: string; description?: string; image?: string; price?: number; categoryId?: number }) =>
    request<boolean>(`/merchant/dishes/${id}`, { method: 'PUT', body: JSON.stringify(payload), headers: { 'Content-Type': 'application/json' } }),
  updateDishAvailability: (id: string | number, isAvailable: boolean) =>
    request<boolean>(`/merchant/dishes/${id}/availability?isAvailable=${isAvailable ? 'true' : 'false'}`, { method: 'POST' }),
  // Shops for admin pages
  getShops: async (page = 1, pageSize = 1000): Promise<PageResult<Shop> | Shop[]> => {
    try {
      return await request<PageResult<Shop>>(`/shops?page=${page}&pageSize=${pageSize}`);
    } catch (e) {
      // 兼容没有 /shops 的场景，回退到 /canteens
      const data = await request<PageResult<Canteen>>(`/canteens?page=${page}&pageSize=${pageSize}`);
      const mapped: Shop[] = (data.list || []).map(c => ({ id: c.id, name: c.name }));
      return { list: mapped, total: mapped.length } as PageResult<Shop>;
    }
  },
  // Users
  getUsers: (page = 1, pageSize = 1000) => request<PageResult<{ id: string | number; name?: string; phone: string; registerDate?: string; status?: string }>>(`/users?page=${page}&pageSize=${pageSize}`),
  userLogin: (phone: string, nickname?: string, avatar?: string) => request(`/users/login`, { method: 'POST', body: JSON.stringify({ phone, nickname, avatar }) }),
};
