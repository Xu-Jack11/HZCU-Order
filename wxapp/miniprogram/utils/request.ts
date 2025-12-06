const BASE_URL = 'https://api.example.com/v1';
const DEFAULT_TIMEOUT = 8000;

interface RequestOptions extends WechatMiniprogram.RequestOption {
  useMock?: boolean;
}

export const request = <T = any>(options: RequestOptions): Promise<T> => {
  return new Promise((resolve, reject) => {
    wx.request({
      ...options,
      url: `${BASE_URL}${options.url}`,
      timeout: options.timeout || DEFAULT_TIMEOUT,
      header: {
        'Content-Type': 'application/json',
        Authorization: wx.getStorageSync('token') || '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data as T);
        } else {
          reject(res);
        }
      },
      fail: (err) => reject(err)
    });
  });
};
