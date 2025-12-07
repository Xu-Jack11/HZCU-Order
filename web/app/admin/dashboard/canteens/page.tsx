'use client';

import { useState } from 'react';
import { Plus, Edit2, Trash2, Power } from 'lucide-react';
import styles from './page.module.css';

interface Canteen {
    id: string;
    name: string;
    address: string;
    managerName: string;
    phone: string;
    status: 'ACTIVE' | 'INACTIVE';
    rating: number;
}

const initialCanteens: Canteen[] = [
    { id: '1', name: '杭州城院第一食堂', address: '南校区生活区', managerName: '张经理', phone: '13800138000', status: 'ACTIVE', rating: 4.8 },
    { id: '2', name: '杭州城院第二食堂', address: '北校区教学楼旁', managerName: '李经理', phone: '13900139000', status: 'ACTIVE', rating: 4.5 },
    { id: '3', name: '清真餐厅', address: '南校区一食堂二楼', managerName: '王经理', phone: '13700137000', status: 'INACTIVE', rating: 4.2 },
];

export default function CanteensPage() {
    const [canteens, setCanteens] = useState<Canteen[]>(initialCanteens);

    const toggleStatus = (id: string) => {
        setCanteens(prev => prev.map(c =>
            c.id === id ? { ...c, status: c.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' } : c
        ));
    };

    const handleDelete = (id: string) => {
        if (confirm('Warning: Deleting a canteen will remove all its associated data. Continue?')) {
            setCanteens(prev => prev.filter(c => c.id !== id));
        }
    }

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>食堂管理</h2>
                <button className={styles.addButton}>
                    <Plus size={18} /> 新增食堂
                </button>
            </div>

            <div className={styles.tableWrapper}>
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>食堂名称/地址</th>
                            <th>负责人</th>
                            <th>联系电话</th>
                            <th>评分</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        {canteens.map(canteen => (
                            <tr key={canteen.id}>
                                <td>
                                    <div className={styles.canteenInfo}>
                                        <span className={styles.canteenName}>{canteen.name}</span>
                                        <span className={styles.canteenAddress}>{canteen.address}</span>
                                    </div>
                                </td>
                                <td>{canteen.managerName}</td>
                                <td>{canteen.phone}</td>
                                <td>⭐ {canteen.rating}</td>
                                <td>
                                    <span className={`${styles.statusBadge} ${canteen.status === 'ACTIVE' ? styles.statusActive : styles.statusInactive}`}>
                                        {canteen.status === 'ACTIVE' ? '营业中' : '已停业'}
                                    </span>
                                </td>
                                <td>
                                    <button
                                        className={styles.actionBtn}
                                        onClick={() => toggleStatus(canteen.id)}
                                        title={canteen.status === 'ACTIVE' ? "打烊" : "开业"}
                                    >
                                        <Power size={14} /> {canteen.status === 'ACTIVE' ? "停业" : "启用"}
                                    </button>
                                    <button className={styles.actionBtn}>
                                        <Edit2 size={14} /> 编辑
                                    </button>
                                    <button className={`${styles.actionBtn} ${styles.deleteBtn}`} onClick={() => handleDelete(canteen.id)}>
                                        <Trash2 size={14} /> 删除
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
