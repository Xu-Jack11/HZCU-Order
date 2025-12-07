'use client';

import { useState } from 'react';
import OrderCard, { Order, OrderStatus } from '@/components/OrderCard';
import styles from './page.module.css';

// Mock Data
const initialOrders: Order[] = [
    {
        id: '1',
        number: 'A001',
        createTime: '11:30',
        status: 'PENDING',
        items: [
            { name: '红烧肉套餐', quantity: 1, spec: '微辣' },
            { name: '可乐', quantity: 1 }
        ],
        totalAmount: 28.5,
        note: '多放点饭'
    },
    {
        id: '2',
        number: 'A002',
        createTime: '11:32',
        status: 'PROCESSING',
        items: [
            { name: '番茄炒蛋', quantity: 1 },
            { name: '米饭', quantity: 1 }
        ],
        totalAmount: 15.0
    },
    {
        id: '3',
        number: 'A003',
        createTime: '11:45',
        status: 'READY',
        items: [
            { name: '牛肉面', quantity: 2, spec: '大碗' }
        ],
        totalAmount: 40.0
    },
    {
        id: '4',
        number: 'A004',
        createTime: '12:01',
        status: 'PENDING',
        items: [
            { name: '麻婆豆腐', quantity: 1 }
        ],
        totalAmount: 12.0
    }
];

export default function DashboardPage() {
    const [orders, setOrders] = useState<Order[]>(initialOrders);
    const [filter, setFilter] = useState<OrderStatus | 'ALL'>('ALL');

    const handleStatusChange = (id: string, newStatus: OrderStatus) => {
        setOrders(prev => prev.map(order =>
            order.id === id ? { ...order, status: newStatus } : order
        ));
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

            {filteredOrders.length === 0 ? (
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
