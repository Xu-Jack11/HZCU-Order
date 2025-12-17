import { CouponItem } from '../../utils/data';
import { clearCouponContext, getAvailableCoupons, getCouponContext, saveSelectedCoupon } from '../../utils/coupon';

interface CouponView extends CouponItem {
  canUse: boolean;
}

Page({
  data: {
    coupons: [] as CouponView[],
    payableAmount: 0,
    shopId: 0
  },

  onShow() {
    this.loadCoupons();
  },

  onUnload() {
    clearCouponContext();
  },

  loadCoupons() {
    const context = getCouponContext();
    const payableAmount = context?.payableAmount || 0;
    const shopId = context?.shopId || 0;
    const coupons = getAvailableCoupons().map((coupon) => ({
      ...coupon,
      canUse: this.checkCouponAvailable(coupon, payableAmount, shopId)
    }));
    this.setData({
      coupons,
      payableAmount,
      shopId
    });
  },

  checkCouponAvailable(coupon: CouponItem, payableAmount: number, shopId?: number) {
    const scopePass = coupon.scope === 'all' || (coupon.shopId && shopId ? coupon.shopId === shopId : false);
    if (!scopePass) return false;
    return payableAmount >= coupon.threshold;
  },

  useCoupon(e: any) {
    const couponId = Number(e.currentTarget.dataset.id);
    const coupon = this.data.coupons.find((item) => item.id === couponId);
    if (!coupon) return;
    if (!coupon.canUse) {
      wx.showToast({
        title: '暂不满足使用条件',
        icon: 'none'
      });
      return;
    }
    saveSelectedCoupon(coupon);
    wx.showToast({
      title: '已选择优惠券',
      icon: 'success',
      duration: 600
    });
    setTimeout(() => {
      wx.navigateBack();
    }, 400);
  },

  clearCoupon() {
    saveSelectedCoupon(null);
    wx.showToast({
      title: '已选择不使用',
      icon: 'none'
    });
    wx.navigateBack();
  }
});
