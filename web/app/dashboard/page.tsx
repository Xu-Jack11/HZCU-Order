'use client';

import { useEffect, useState } from 'react';
import OrderCard, { Order, OrderStatus } from '@/components/OrderCard';
import { api, PageResult } from '@/lib/api';
import styles from './page.module.css';

// 改为从后端接口加载数据

export default function DashboardPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [filter, setFilter] = useState<OrderStatus | 'ALL'>('ALL');
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const loadOrders = async (status?: OrderStatus) => {
        setLoading(true);
        setError(null);
        try {
            const page: PageResult<any> = await api.getOrders(status ? { status } : undefined);
            // 将后端订单结构规范化为前端使用的结构
            const normalized: Order[] = (page.list || []).map((o: any) => {
                // 后端状态: pending|preparing|ready|completed|canceled → 前端: PENDING|PROCESSING|READY|COMPLETED|CANCELLED
                const statusMap: Record<string, OrderStatus> = {
                    pending: 'PENDING',
                    preparing: 'PROCESSING',
                    ready: 'READY',
                    completed: 'COMPLETED',
                    canceled: 'CANCELLED',
                };
                const status = statusMap[String(o.status || '').toLowerCase()] || 'PENDING';
                return {
                    id: String(o.id),
                    number: String(o.number || o.id),
                    createTime: String(o.createTime || ''),
                    status,
                    items: Array.isArray(o.items) ? o.items : undefined,
                    goods: Array.isArray(o.goods) ? o.goods : undefined,
                    totalAmount: typeof o.totalAmount === 'number' ? o.totalAmount : (typeof o.totalPrice === 'number' ? o.totalPrice : 0),
                    note: o.remark || o.note || '',
                } as Order;
            });
            setOrders(normalized);
        } catch (e: any) {
            setError(e?.message || '加载订单失败');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (filter === 'ALL') {
            loadOrders();
        } else {
            loadOrders(filter as OrderStatus);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [filter]);

    const handleStatusChange = async (id: string, newStatus: OrderStatus) => {
        try {
            const updated = await api.updateOrderStatus(id, newStatus);
            setOrders(prev => prev.map(order =>
                order.id === id ? updated : order
            ));
        } catch (e: any) {
            setError(e?.message || '更新订单状态失败');
        }
    };

    const filteredOrders = orders.filter(o =>
        filter === 'ALL'
            ? o.status !== 'COMPLETED' && o.status !== 'CANCELLED' // Default view hides completed/cancelled
            : o.status === filter
    );

    const stats = {
        pending: orders.filter(o => o.status === 'PENDING').length,
        processing: orders.filter(o => o.status === 'PROCESSING').length,
        ready: orders.filter(o => o.status === 'READY').length,
    }

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
                    className={`${styles.filterBtn} ${filter === 'PENDING' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('PENDING')}
                >
                    待接单 ({stats.pending})
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'PROCESSING' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('PROCESSING')}
                >
                    制作中 ({stats.processing})
                </button>
                <button
                    className={`${styles.filterBtn} ${filter === 'READY' ? styles.filterBtnActive : ''}`}
                    onClick={() => setFilter('READY')}
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

            {error && (
                <div className={styles.emptyState} style={{ color: '#c00' }}>{error}</div>
            )}
            {loading ? (
                <div className={styles.emptyState}>加载中...</div>
            ) : filteredOrders.length === 0 ? (
                <div className={styles.emptyState}>暂无相关订单</div>
            ) : (
                <div className={styles.grid}>
                    {filteredOrders.map(order => (
                        <OrderCard
                            key={order.id}
                            order={order}
                            onStatusChange={handleStatusChange}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
