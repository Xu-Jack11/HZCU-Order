// checkout.ts
// 结算页面
import { clearCartSnapshot } from '../../utils/cart';
import { CouponItem } from '../../utils/data';
import { clearCouponContext, getSelectedCoupon, saveSelectedCoupon, setCouponContext } from '../../utils/coupon';
import { createOrder, payOrder } from '../../utils/api';

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
    selectedCoupon: null as CouponItem | null,
    paymentMethod: 'WECHAT' as 'WECHAT' | 'BALANCE'
  },

  onLoad() {
    this.loadCartData();
  },

  onShow() {
    this.syncSelectedCoupon();
  },

  // 选择支付方式
  choosePaymentMethod() {
    const itemList = ['微信支付', '余额支付'];
    wx.showActionSheet({
      itemList,
      success: (res) => {
        const paymentMethod = res.tapIndex === 0 ? 'WECHAT' : 'BALANCE';
        this.setData({ paymentMethod });
      }
    });
  },

  // 计算打包费
  getPackingFee(diningMode: string): number {
    return diningMode === DINING_MODES.TAKEAWAY ? PACKING_FEE : 0;
  },

  // 计算总格
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

  // 输入桌格
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

  // 提交订单并支付
  async submitOrder() {
    wx.showLoading({ title: '提交中...' });

    try {
      // 1. 创建订单
      const orderResult = await createOrder({
        shopId: this.data.shopInfo?.id || 0,
        cartList: this.data.cartList,
        totalPrice: this.data.finalPrice,
        diningMode: this.data.diningMode === 'dine-in' ? 'DINE_IN' : 'TAKEAWAY',
        paymentMethod: this.data.paymentMethod,
        tableNo: this.data.tableNo,
        pickupTime: this.data.pickupTime,
        remark: this.data.remark
      });

      const orderId = orderResult.orderId;

      // 2. 发起支付
      wx.setNavigationBarTitle({ title: '正在支付...' });
      const payResult = await payOrder(orderId, this.data.paymentMethod);

      // 3. 模拟微信支付成功 (在真实环境下对于 WECHAT 需调用 wx.requestPayment(payParams))
      // 目前后端在 createPayment 时会自动 processPayment 并更新状态

      wx.showToast({
        title: '支付完成',
        icon: 'success'
      });

      // 清理工作
      wx.removeStorageSync('cartData');
      clearCartSnapshot(this.data.shopInfo?.id || 0);
      saveSelectedCoupon(null);
      clearCouponContext();

      setTimeout(() => {
        wx.switchTab({
          url: '/pages/order/order'
        });
      }, 1500);

    } catch (error) {
      console.error(error);
      wx.showToast({
        title: '操作失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  }
});
