// checkout.ts
// 结算页面

Page({
  data: {
    address: null as any,
    shopInfo: {} as any,
    cartList: [] as any[],
    totalPrice: 0,
    totalCount: 0,
    deliveryTime: '',
    remark: '',
    couponDiscount: 0,
    finalPrice: 0
  },

  onLoad() {
    this.loadCartData();
    this.loadDefaultAddress();
  },

  // 加载购物车数据
  loadCartData() {
    const cartData = wx.getStorageSync('cartData');
    if (cartData) {
      const finalPrice = cartData.totalPrice + cartData.shopInfo.deliveryFee - this.data.couponDiscount;
      this.setData({
        shopInfo: cartData.shopInfo,
        cartList: cartData.cartList,
        totalPrice: cartData.totalPrice,
        totalCount: cartData.totalCount,
        finalPrice: Math.round(finalPrice * 100) / 100
      });
    }
  },

  // 加载默认地址
  loadDefaultAddress() {
    const address = wx.getStorageSync('defaultAddress');
    if (address) {
      this.setData({ address });
    }
  },

  // 选择地址
  chooseAddress() {
    wx.chooseAddress({
      success: (res) => {
        const address = {
          name: res.userName,
          phone: res.telNumber,
          province: res.provinceName,
          city: res.cityName,
          district: res.countyName,
          detail: res.detailInfo
        };
        wx.setStorageSync('defaultAddress', address);
        this.setData({ address });
      }
    });
  },

  // 选择配送时间
  chooseTime() {
    wx.showActionSheet({
      itemList: ['立即配送', '12:00-13:00', '13:00-14:00', '18:00-19:00', '19:00-20:00'],
      success: (res) => {
        const times = ['立即配送', '12:00-13:00', '13:00-14:00', '18:00-19:00', '19:00-20:00'];
        this.setData({
          deliveryTime: times[res.tapIndex]
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
    if (!this.data.address) {
      wx.showToast({
        title: '请选择收货地址',
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
