'use client';

import { useState, useEffect } from 'react';
import OrderCard, { Order, OrderStatus } from '@/components/OrderCard';
import styles from './page.module.css';
import { api } from '@/lib/api';

export default function DashboardPage() {
    const [orders, setOrders] = useState<any[]>([]);
    const [statsOrders, setStatsOrders] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState<string>('ALL');

    const fetchOrders = async () => {
        setLoading(true);
        try {
            // Fetch all orders for stats
            const statsRes = await api.orders.getMerchantOrders({});
            if (statsRes.success) {
                setStatsOrders(statsRes.data);
            }

            // Fetch filtered orders for display
            const params: any = {};
            if (filter !== 'ALL') {
                params.status = filter;
            }
            const res = await api.orders.getMerchantOrders(params);
            if (res.success) {
                setOrders(res.data);
            }
        } catch (err) {
            console.error('Failed to fetch orders', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders();
        const timer = setInterval(fetchOrders, 30000);
        return () => clearInterval(timer);
    }, [filter]);

    const handleStatusChange = async (id: string, action: string) => {
        try {
            const res = await api.orders.updateStatus(id, action);
            if (res.success) {
                fetchOrders();
            }
        } catch (err) {
            alert('操作失败');
        }
    };

    const stats = {
        pending: statsOrders.filter(o => o.status === 'PAID').length,
        processing: statsOrders.filter(o => o.status === 'PREPARING').length,
        ready: statsOrders.filter(o => o.status === 'READY_FOR_PICKUP').length,
    }

    const mapStatusToUI = (status: string): OrderStatus => {
        switch (status) {
            case 'PAID': return 'PENDING';
            case 'PREPARING': return 'PROCESSING';
            case 'READY_FOR_PICKUP': return 'READY';
            case 'COMPLETED': return 'COMPLETED';
            case 'CANCELLED': return 'CANCELLED';
            default: return 'COMPLETED';
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.filters}>
                <button
                    className={`${styles.filterBtn} ${filter === 'ALL' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('ALL')}
                >
                    全部进行中
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'PAID' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('PAID')}
                >
                    新订单 ({stats.pending})
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'PREPARING' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('PREPARING')}
                >
                    制作中 ({stats.processing})
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'READY_FOR_PICKUP' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('READY_FOR_PICKUP')}
                >
                    待取餐 ({stats.ready})
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'COMPLETED' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('COMPLETED')}
                >
                    已完成
                </button>
            </div>

            {loading && orders.length === 0 ? (
                <div className={styles.loading}>加载中...</div>
            ) : orders.length === 0 ? (
                <div className={styles.emptyState}>暂无相关订单</div>
            ) : (
                <div className={styles.grid}>
                    {orders.map(order => (
                        <OrderCard
                            key={order.orderId}
                            order={{
                                id: order.orderId.toString(),
                                number: order.pickupCode || '---',
                                createTime: new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
                                status: mapStatusToUI(order.status),
                                items: (order.items || []).map((it: any) => ({
                                    name: it.dishName,
                                    quantity: it.quantity,
                                    spec: it.specName
                                })),
                                totalAmount: order.totalAmount,
                                note: order.remark
                            }}
                            onStatusChange={(id, status) => {
                                let action = '';
                                if (status === 'PROCESSING') action = 'accept';
                                else if (status === 'READY') action = 'finish';
                                else if (status === 'COMPLETED') action = 'complete';

                                if (action) handleStatusChange(id, action);
                            }}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
