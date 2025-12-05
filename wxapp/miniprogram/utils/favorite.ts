import { ShopItem } from './data';

const FAVORITE_KEY = 'favoriteShops';

const normalizeList = (list: ShopItem[]) => {
  const map = new Map<number, ShopItem>();
  list.forEach((item) => {
    map.set(item.id, item);
  });
  return Array.from(map.values());
};

export const getFavoriteShops = (): ShopItem[] => {
  const stored = wx.getStorageSync(FAVORITE_KEY);
  return (stored as ShopItem[]) || [];
};

const saveFavoriteShops = (list: ShopItem[]) => {
  wx.setStorageSync(FAVORITE_KEY, normalizeList(list));
};

export const isFavoriteShop = (shopId: number) => {
  const stored = getFavoriteShops();
  return stored.some((item) => item.id === shopId);
};

export const toggleFavoriteShop = (shop: ShopItem) => {
  const stored = getFavoriteShops();
  const exists = stored.find((item) => item.id === shop.id);
  const nextList = exists ? stored.filter((item) => item.id !== shop.id) : [...stored, shop];
  saveFavoriteShops(nextList);
  return nextList;
};

export const removeFavoriteShop = (shopId: number) => {
  const stored = getFavoriteShops();
  const nextList = stored.filter((item) => item.id !== shopId);
  saveFavoriteShops(nextList);
  return nextList;
};
