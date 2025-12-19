const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

async function request(path: string, options: RequestInit = {}) {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
    };

    const response = await fetch(`${BASE_URL}${path}`, {
        ...options,
        headers,
    });

    const resData = await response.json();

    if (!response.ok) {
        throw new Error(resData.message || 'Request failed');
    }

    return resData;
}

export const api = {
    auth: {
        loginMerchant: (data: any) => request('/auth/login/merchant', {
            method: 'POST',
            body: JSON.stringify(data),
        }),
        loginAdmin: (data: any) => request('/auth/login/admin', {
            method: 'POST',
            body: JSON.stringify(data),
        }),
    },
    orders: {
        getMerchantOrders: (params: any) => {
            const qs = new URLSearchParams(params).toString();
            return request(`/merchant/orders?${qs}`);
        },
        updateStatus: (id: string, action: string) => request(`/merchant/orders/${id}/${action}`, {
            method: 'PATCH',
        }),
    },
    dishes: {
        getMyDishes: () => request('/merchant/dishes'),
        getMyCategories: () => request('/merchant/dishes/categories'),
        addDish: (data: any) => request('/merchant/dishes', {
            method: 'POST',
            body: JSON.stringify(data),
        }),
        updateDish: (id: string, data: any) => request(`/merchant/dishes/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data),
        }),
        deleteDish: (id: string) => request(`/merchant/dishes/${id}`, {
            method: 'DELETE',
        }),
        updateStatus: (id: string, status: number) => request(`/merchant/dishes/${id}/status?status=${status}`, {
            method: 'PATCH',
        }),
    },
    stats: {
        getMerchantStats: (days: number = 7) => request(`/statistics/merchant?days=${days}`),
        getAdminStats: (canteenId: string, days: number = 7) => request(`/statistics/admin?canteenId=${canteenId}&days=${days}`),
        getPlatformStats: () => request('/statistics/platform'),
    },
    admin: {
        getUsers: () => request('/admin/users'),
        updateUserStatus: (id: string, status: number) => request(`/admin/users/${id}/status?status=${status}`, {
            method: 'PATCH',
        }),
        getCanteens: () => request('/admin/canteens'),
        createCanteen: (data: any) => request('/admin/canteens', {
            method: 'POST',
            body: JSON.stringify(data),
        }),
        updateCanteen: (id: string, data: any) => request(`/admin/canteens/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data),
        }),
        createMerchant: (data: any) => request('/admin/merchants', {
            method: 'POST',
            body: JSON.stringify(data),
        }),
        getMerchantAccounts: (canteenId: string) => request(`/admin/merchants/${canteenId}/accounts`),
        resetMerchantPassword: (accountId: string, newPassword: string) => request(`/admin/merchants/accounts/${accountId}/password?newPassword=${encodeURIComponent(newPassword)}`, {
            method: 'PATCH',
        }),
    }
};
