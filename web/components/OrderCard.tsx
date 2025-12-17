'use client';

import { Clock, CheckCircle2, XCircle, ChefHat, Bell } from 'lucide-react';
import styles from './OrderCard.module.css';

// Type definitions matching API mostly
export type OrderStatus = 'PENDING' | 'PROCESSING' | 'READY' | 'COMPLETED' | 'CANCELLED';

export interface OrderItem {
    name: string;
    quantity: number;
    spec?: string;
}

// 兼容后端返回的 goods 项结构
export interface BackendGoodsItem {
    name: string;
    image?: string;
    price?: number;
    count?: number;
}

export interface Order {
    id: string;
    number: string; // Pick-up number
    createTime: string;
    status: OrderStatus;
    items: OrderItem[];
    // 兼容后端字段
    goods?: BackendGoodsItem[];
    totalAmount: number;
    note?: string;
}

interface OrderCardProps {
    order: Order;
    onStatusChange: (id: string, newStatus: OrderStatus) => void;
}

export default function OrderCard({ order, onStatusChange }: OrderCardProps) {
    const statusColors = {
        PENDING: styles.status_pending,
        PROCESSING: styles.status_processing,
        READY: styles.status_ready,
        COMPLETED: styles.status_completed,
        CANCELLED: styles.status_completed,
    };

    const statusText = {
        PENDING: '待接单',
        PROCESSING: '制作中',
        READY: '待取餐',
        COMPLETED: '已完成',
        CANCELLED: '已取消',
    };

    return (
        <div className={styles.card}>
            <div className={styles.header}>
                <div>
                    <div className={styles.orderId}>#{order.number}</div>
                    <div className={styles.time}>{order.createTime}</div>
                </div>
                <span className={`${styles.status} ${statusColors[order.status] || styles.status_completed}`}>
                    {statusText[order.status]}
                </span>
            </div>

            <div className={styles.items}>
                {(order.items && order.items.length > 0
                    ? order.items
                    : (order.goods || []).map(g => ({ name: g.name, quantity: g.count || 0 } as OrderItem))
                ).map((item, idx) => (
                    <div key={idx} className={styles.item}>
                        <span className={styles.itemName}>
                            {item.name} {item.spec && <span style={{ fontSize: '0.8em', color: '#666' }}>({item.spec})</span>}
                        </span>
                        <span className={styles.itemQuantity}>x{item.quantity}</span>
                    </div>
                ))}
                {order.note && (
                    <div style={{ fontSize: '0.8rem', color: '#f59e0b', marginTop: '0.5rem' }}>
                        备注: {order.note}
                    </div>
                )}
            </div>

            <div className={styles.footer}>
                {order.status === 'PENDING' && (
                    <>
                        <button
                            className={`${styles.button} ${styles.btnSecondary}`}
                            onClick={() => onStatusChange(order.id, 'CANCELLED')}
                        >
                            <XCircle size={16} /> 拒单
                        </button>
                        <button
                            className={`${styles.button} ${styles.btnPrimary}`}
                            onClick={() => onStatusChange(order.id, 'PROCESSING')}
                        >
                            <ChefHat size={16} /> 接单
                        </button>
                    </>
                )}
                {order.status === 'PROCESSING' && (
                    <button
                        className={`${styles.button} ${styles.btnPrimary}`}
                        onClick={() => onStatusChange(order.id, 'READY')}
                    >
                        <Bell size={16} /> 叫号取餐
                    </button>
                )}
                {order.status === 'READY' && (
                    <button
                        className={`${styles.button} ${styles.btnPrimary}`}
                        onClick={() => onStatusChange(order.id, 'COMPLETED')}
                    >
                        <CheckCircle2 size={16} /> 完成订单
                    </button>
                )}
                {order.status === 'COMPLETED' && (
                    <div style={{ width: '100%', textAlign: 'center', fontSize: '0.875rem', color: '#999' }}>订单已归档</div>
                )}
            </div>
        </div>
    );
}
