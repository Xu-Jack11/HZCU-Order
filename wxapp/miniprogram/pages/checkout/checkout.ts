// checkout.ts
// 结算页面
import { clearCartSnapshot } from '../../utils/cart';
import { CouponItem } from '../../utils/data';
import { clearCouponContext, getSelectedCoupon, saveSelectedCoupon, setCouponContext } from '../../utils/coupon';
import { createOrder } from '../../utils/api';

// 常量定义
const PACKING_FEE = 2;
const DINING_MODES = {
  DINE_IN: 'dine-in' as const,
  TAKEAWAY: 'takeaway' as const
};
const DINING_MODE_LABELS = ['堂食', '打包'];
const PICKUP_TIMES = ['立即取餐', '12:00-13:00', '13:00-14:00', '18:00-19:00', '19:00-20:00'];

Page({
  data: {
    diningMode: DINING_MODES.DINE_IN as 'dine-in' | 'takeaway',
    tableNo: '',
    shopInfo: {} as any,
    cartList: [] as any[],
    totalPrice: 0,
    totalCount: 0,
    pickupTime: '',
    remark: '',
    packingFee: 0,
    couponDiscount: 0,
    finalPrice: 0,
    selectedCoupon: null as CouponItem | null
  },

  onLoad() {
    this.loadCartData();
  },

  onShow() {
    this.syncSelectedCoupon();
  },

  // 计算打包费
  getPackingFee(diningMode: string): number {
    return diningMode === DINING_MODES.TAKEAWAY ? PACKING_FEE : 0;
  },

  // 计算总价
  calculateFinalPrice() {
    const packingFee = this.getPackingFee(this.data.diningMode);
    const couponDiscount = this.getCouponDiscount(packingFee);
    const finalPrice = this.data.totalPrice + packingFee - couponDiscount;
    this.setData({
      packingFee,
      couponDiscount,
      finalPrice: Math.round(finalPrice * 100) / 100
    });
  },

  getCouponDiscount(packingFee: number) {
    const coupon = this.data.selectedCoupon;
    if (!coupon || !this.data.shopInfo?.id) return 0;
    if (coupon.scope === 'shop' && coupon.shopId && coupon.shopId !== this.data.shopInfo.id) {
      return 0;
    }
    const payable = this.data.totalPrice + packingFee;
    return payable >= coupon.threshold ? coupon.discount : 0;
  },

  syncSelectedCoupon() {
    const coupon = getSelectedCoupon();
    if (!coupon || !this.data.shopInfo?.id) {
      this.setData(
        {
          selectedCoupon: null,
          couponDiscount: 0
        },
        () => this.calculateFinalPrice()
      );
      return;
    }
    if (coupon.scope === 'shop' && coupon.shopId && coupon.shopId !== this.data.shopInfo.id) {
      this.setData(
        {
          selectedCoupon: null,
          couponDiscount: 0
        },
        () => this.calculateFinalPrice()
      );
      return;
    }
    this.setData(
      {
        selectedCoupon: coupon
      },
      () => this.calculateFinalPrice()
    );
  },

  // 加载购物车数据
  loadCartData() {
    const cartData = wx.getStorageSync('cartData');
    if (cartData) {
      this.setData(
        {
          shopInfo: cartData.shopInfo,
          cartList: cartData.cartList,
          totalPrice: cartData.totalPrice,
          totalCount: cartData.totalCount
        },
        () => {
          this.syncSelectedCoupon();
        }
      );
    }
  },

  // 选择就餐方式
  chooseDiningMode() {
    wx.showActionSheet({
      itemList: DINING_MODE_LABELS,
      success: (res) => {
        const diningMode = res.tapIndex === 0 ? DINING_MODES.DINE_IN : DINING_MODES.TAKEAWAY;
        this.setData(
          {
            diningMode
          },
          () => {
            this.calculateFinalPrice();
          }
        );
      }
    });
  },

  // 输入桌号
  onTableNoInput(e: any) {
    this.setData({
      tableNo: e.detail.value
    });
  },

  // 选择取餐时间
  choosePickupTime() {
    wx.showActionSheet({
      itemList: PICKUP_TIMES,
      success: (res) => {
        this.setData({
          pickupTime: PICKUP_TIMES[res.tapIndex]
        });
      }
    });
  },

  // 选择优惠券
  chooseCoupon() {
    const payableAmount = this.data.totalPrice + this.getPackingFee(this.data.diningMode);
    setCouponContext({
      shopId: this.data.shopInfo?.id,
      payableAmount
    });
    wx.navigateTo({
      url: '/pages/coupon/coupon'
    });
  },

  // 输入备注
  onRemarkInput(e: any) {
    this.setData({
      remark: e.detail.value
    });
  },

  // 提交订单
  async submitOrder() {
    if (this.data.diningMode === DINING_MODES.DINE_IN && !this.data.tableNo) {
      wx.showToast({
        title: '请输入桌号',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '提交中...' });

    try {
      await createOrder({
        shopId: this.data.shopInfo?.id || 0,
        cartList: this.data.cartList,
        totalPrice: this.data.finalPrice,
        diningMode: this.data.diningMode,
        tableNo: this.data.tableNo,
        pickupTime: this.data.pickupTime,
        remark: this.data.remark
      });
      wx.removeStorageSync('cartData');
      clearCartSnapshot(this.data.shopInfo?.id || 0);
      saveSelectedCoupon(null);
      clearCouponContext();

      wx.showToast({
        title: '下单成功',
        icon: 'success',
        duration: 1500,
        success: () => {
          setTimeout(() => {
            wx.switchTab({
              url: '/pages/order/order'
            });
          }, 1500);
        }
      });
    } catch (error) {
      wx.showToast({
        title: '下单失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  }
});
