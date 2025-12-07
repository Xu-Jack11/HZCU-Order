'use client';

import { useState } from 'react';
import { Plus, Edit2, Trash2, Power, Store, X } from 'lucide-react';
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

interface Merchant {
    id: string;
    name: string;
    status: 'OPERATING' | 'CLOSED';
}

const initialCanteens: Canteen[] = [
    { id: '1', name: 'HZCU No.1 Canteen', address: 'South Campus Living Area', managerName: 'Manager Zhang', phone: '13800138000', status: 'ACTIVE', rating: 4.8 },
    { id: '2', name: 'HZCU No.2 Canteen', address: 'North Campus Teaching Building', managerName: 'Manager Li', phone: '13900139000', status: 'ACTIVE', rating: 4.5 },
    { id: '3', name: 'Muslim Restaurant', address: 'No.1 Canteen 2nd Floor', managerName: 'Manager Wang', phone: '13700137000', status: 'INACTIVE', rating: 4.2 },
];

// Mock merchants data
const mockMerchants: Record<string, Merchant[]> = {
    '1': [
        { id: 'm1', name: 'Zhang\'s Noodle House', status: 'OPERATING' },
        { id: 'm2', name: 'Delicious Clay Pot', status: 'OPERATING' },
    ],
    '2': [
        { id: 'm3', name: 'Juicy Burger', status: 'OPERATING' },
    ],
    '3': [],
};

export default function CanteensPage() {
    const [canteens, setCanteens] = useState<Canteen[]>(initialCanteens);
    const [selectedCanteenId, setSelectedCanteenId] = useState<string | null>(null);
    const [merchants, setMerchants] = useState<Record<string, Merchant[]>>(mockMerchants);

    const [editingCanteen, setEditingCanteen] = useState<Canteen | null>(null);
    const [editForm, setEditForm] = useState({ phone: '', managerName: '' });

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

    const handleManageMerchants = (canteenId: string) => {
        setSelectedCanteenId(canteenId);
    };

    const handleEditCanteen = (canteen: Canteen) => {
        setEditingCanteen(canteen);
        setEditForm({ phone: canteen.phone, managerName: canteen.managerName });
    };

    const saveCanteenEdit = () => {
        if (!editingCanteen) return;
        setCanteens(prev => prev.map(c =>
            c.id === editingCanteen.id ? { ...c, phone: editForm.phone, managerName: editForm.managerName } : c
        ));
        setEditingCanteen(null);
    };

    const closeModal = () => {
        setSelectedCanteenId(null);
        setEditingCanteen(null);
    };

    const removeMerchant = (canteenId: string, merchantId: string) => {
        if (!confirm('Are you sure you want to remove this merchant?')) return;
        setMerchants(prev => ({
            ...prev,
            [canteenId]: prev[canteenId].filter(m => m.id !== merchantId)
        }));
    };

    const activeCanteen = selectedCanteenId ? canteens.find(c => c.id === selectedCanteenId) : null;

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
                                    <button
                                        className={styles.actionBtn}
                                        onClick={() => handleManageMerchants(canteen.id)}
                                        title="管理商家"
                                    >
                                        <Store size={14} /> 商家
                                    </button>
                                    <button
                                        className={styles.actionBtn}
                                        onClick={() => handleEditCanteen(canteen)}
                                    >
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

            {/* Merchant Management Modal */}
            {selectedCanteenId && activeCanteen && (
                <div className={styles.modalOverlay} onClick={(e) => {
                    if (e.target === e.currentTarget) closeModal();
                }}>
                    <div className={styles.modal}>
                        <div className={styles.modalHeader}>
                            <h3 className={styles.modalTitle}>商家管理 - {activeCanteen.name}</h3>
                            <button className={styles.closeBtn} onClick={closeModal}>
                                <X size={20} />
                            </button>
                        </div>
                        <div className={styles.modalContent}>
                            <div style={{ marginBottom: '1rem', display: 'flex', justifyContent: 'flex-end' }}>
                                <button className={styles.addButton} style={{ fontSize: '0.85rem', padding: '0.4rem 0.8rem' }}>
                                    <Plus size={16} /> 添加商家账号
                                </button>
                            </div>

                            {(merchants[selectedCanteenId] || []).length === 0 ? (
                                <div style={{ textAlign: 'center', padding: '2rem', color: 'var(--muted-foreground)' }}>
                                    该食堂暂无入驻商家
                                </div>
                            ) : (
                                (merchants[selectedCanteenId] || []).map(merchant => (
                                    <div key={merchant.id} className={styles.merchantItem}>
                                        <div className={styles.merchantInfo}>
                                            <span className={styles.merchantName}>{merchant.name}</span>
                                            <span className={styles.merchantStatus}>状态: {merchant.status}</span>
                                        </div>
                                        <button
                                            className={`${styles.actionBtn} ${styles.deleteBtn}`}
                                            onClick={() => removeMerchant(selectedCanteenId, merchant.id)}
                                        >
                                            <Trash2 size={14} /> 移除
                                        </button>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>
                </div>
            )}

            {/* Edit Canteen Modal */}
            {editingCanteen && (
                <div className={styles.modalOverlay} onClick={(e) => {
                    if (e.target === e.currentTarget) closeModal();
                }}>
                    <div className={styles.modal} style={{ width: '400px' }}>
                        <div className={styles.modalHeader}>
                            <h3 className={styles.modalTitle}>编辑食堂信息</h3>
                            <button className={styles.closeBtn} onClick={closeModal}>
                                <X size={20} />
                            </button>
                        </div>
                        <div className={styles.modalContent}>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        食堂名称
                                    </label>
                                    <input
                                        type="text"
                                        value={editingCanteen.name}
                                        disabled
                                        style={{ width: '100%', padding: '0.5rem', borderRadius: '0.375rem', border: '1px solid var(--border)', background: 'var(--muted)' }}
                                    />
                                </div>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        负责人姓名
                                    </label>
                                    <input
                                        type="text"
                                        value={editForm.managerName}
                                        onChange={e => setEditForm({ ...editForm, managerName: e.target.value })}
                                        style={{ width: '100%', padding: '0.5rem', borderRadius: '0.375rem', border: '1px solid var(--border)' }}
                                    />
                                </div>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        联系电话
                                    </label>
                                    <input
                                        type="text"
                                        value={editForm.phone}
                                        onChange={e => setEditForm({ ...editForm, phone: e.target.value })}
                                        style={{ width: '100%', padding: '0.5rem', borderRadius: '0.375rem', border: '1px solid var(--border)' }}
                                    />
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.5rem', marginTop: '1rem' }}>
                                    <button
                                        onClick={closeModal}
                                        style={{ padding: '0.5rem 1rem', borderRadius: '0.375rem', border: '1px solid var(--border)', background: 'transparent', cursor: 'pointer' }}
                                    >
                                        取消
                                    </button>
                                    <button
                                        onClick={saveCanteenEdit}
                                        style={{ padding: '0.5rem 1rem', borderRadius: '0.375rem', border: 'none', background: 'var(--primary)', color: 'white', cursor: 'pointer' }}
                                    >
                                        保存
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}

        </div>
    );
}
