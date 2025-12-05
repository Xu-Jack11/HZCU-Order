interface CartStore {
  [shopId: string]: CartSnapshot;
}

export interface CartSnapshot {
  shopId: number;
  shopInfo: any;
  cartList: any[];
  totalPrice: number;
  totalCount: number;
}

const CART_KEY = 'cartStore';

const getStore = () => {
  const store = wx.getStorageSync(CART_KEY);
  return (store as CartStore) || {};
};

const saveStore = (store: CartStore) => {
  wx.setStorageSync(CART_KEY, store);
};

export const getCartSnapshot = (shopId: number): CartSnapshot | null => {
  const store = getStore();
  return store[shopId] || null;
};

export const saveCartSnapshot = (snapshot: CartSnapshot) => {
  const store = getStore();
  if (snapshot.totalCount <= 0) {
    delete store[snapshot.shopId];
  } else {
    store[snapshot.shopId] = snapshot;
  }
  saveStore(store);
};

export const clearCartSnapshot = (shopId: number) => {
  const store = getStore();
  delete store[shopId];
  saveStore(store);
};
