import { CouponItem, defaultCoupons } from './data';

const COUPON_KEY = 'availableCoupons';
const SELECTED_COUPON_KEY = 'selectedCoupon';
const COUPON_CONTEXT_KEY = 'couponContext';

export const getAvailableCoupons = () => {
  const stored = wx.getStorageSync(COUPON_KEY) as CouponItem[] | undefined;
  if (stored && stored.length > 0) {
    return stored;
  }
  wx.setStorageSync(COUPON_KEY, defaultCoupons);
  return defaultCoupons;
};

export const getSelectedCoupon = () => {
  const stored = wx.getStorageSync(SELECTED_COUPON_KEY) as CouponItem | undefined;
  return stored || null;
};

export const saveSelectedCoupon = (coupon: CouponItem | null) => {
  if (coupon) {
    wx.setStorageSync(SELECTED_COUPON_KEY, coupon);
  } else {
    wx.removeStorageSync(SELECTED_COUPON_KEY);
  }
};

export const setCouponContext = (context: { shopId?: number; payableAmount?: number }) => {
  wx.setStorageSync(COUPON_CONTEXT_KEY, context);
};

export const getCouponContext = () => {
  const stored = wx.getStorageSync(COUPON_CONTEXT_KEY) as { shopId?: number; payableAmount?: number } | undefined;
  return stored || null;
};

export const clearCouponContext = () => {
  wx.removeStorageSync(COUPON_CONTEXT_KEY);
};
