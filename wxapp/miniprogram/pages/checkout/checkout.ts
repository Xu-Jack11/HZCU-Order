// checkout.ts
// 结算页面

// 打包费常量
const PACKING_FEE = 2;

Page({
  data: {
    diningMode: 'dine-in' as 'dine-in' | 'takeaway',
    tableNo: '',
    shopInfo: {} as any,
    cartList: [] as any[],
    totalPrice: 0,
    totalCount: 0,
    pickupTime: '',
    remark: '',
    packingFee: 0,
    couponDiscount: 0,
    finalPrice: 0
  },

  onLoad() {
    this.loadCartData();
  },

  // 计算总价
  calculateFinalPrice() {
    const packingFee = this.data.diningMode === 'takeaway' ? PACKING_FEE : 0;
    const finalPrice = this.data.totalPrice + packingFee - this.data.couponDiscount;
    this.setData({
      packingFee,
      finalPrice: Math.round(finalPrice * 100) / 100
    });
  },

  // 加载购物车数据
  loadCartData() {
    const cartData = wx.getStorageSync('cartData');
    if (cartData) {
      this.setData({
        shopInfo: cartData.shopInfo,
        cartList: cartData.cartList,
        totalPrice: cartData.totalPrice,
        totalCount: cartData.totalCount
      }, () => {
        this.calculateFinalPrice();
      });
    }
  },

  // 选择就餐方式
  chooseDiningMode() {
    wx.showActionSheet({
      itemList: ['堂食', '打包'],
      success: (res) => {
        const modes: Array<'dine-in' | 'takeaway'> = ['dine-in', 'takeaway'];
        this.setData({
          diningMode: modes[res.tapIndex]
        }, () => {
          this.calculateFinalPrice();
        });
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
      itemList: ['立即取餐', '12:00-13:00', '13:00-14:00', '18:00-19:00', '19:00-20:00'],
      success: (res) => {
        const times = ['立即取餐', '12:00-13:00', '13:00-14:00', '18:00-19:00', '19:00-20:00'];
        this.setData({
          pickupTime: times[res.tapIndex]
        });
      }
    });
  },

  // 输入备注
  onRemarkInput(e: any) {
    this.setData({
      remark: e.detail.value
    });
  },

  // 提交订单
  submitOrder() {
    if (this.data.diningMode === 'dine-in' && !this.data.tableNo) {
      wx.showToast({
        title: '请输入桌号',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '提交中...' });

    // 模拟订单提交
    setTimeout(() => {
      wx.hideLoading();
      
      // 清空购物车数据
      wx.removeStorageSync('cartData');
      
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
    }, 1000);
  }
});
