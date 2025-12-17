'use client';

import { useEffect, useState } from 'react';
import { Plus, Edit2, Trash2, Power, Store, X } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';

interface Canteen {
    id: string;
    name: string;
    address?: string;
    managerName?: string;
    phone?: string;
    status: 'ACTIVE' | 'INACTIVE';
    rating?: number;
    monthlySales?: number;
    logo?: string;
}

interface Merchant {
    id: string;
    name: string;
    status: 'OPERATING' | 'CLOSED';
}

const initialCanteens: Canteen[] = [];

// Mock merchants data
const mockMerchants: Record<string, Merchant[]> = {
    '1': [
        { id: 'm1', name: '前台账号', status: 'OPERATING' },
        { id: 'm2', name: '后厨账号', status: 'OPERATING' },
    ],
    '2': [
        { id: 'm3', name: '堂食窗口', status: 'OPERATING' },
    ],
    '3': [
        { id: 'm4', name: '饮品吧台', status: 'OPERATING' },
    ],
    '4': [
        { id: 'm5', name: '外卖账号', status: 'OPERATING' },
    ],
};

export default function CanteensPage() {
    const [canteens, setCanteens] = useState<Canteen[]>(initialCanteens);
    const [selectedCanteenId, setSelectedCanteenId] = useState<string | null>(null);
    const [merchants, setMerchants] = useState<Record<string, Merchant[]>>(mockMerchants);

    const [editingCanteen, setEditingCanteen] = useState<Canteen | null>(null);
    const [editForm, setEditForm] = useState({ phone: '', managerName: '' });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const load = async () => {
            setLoading(true);
            setError(null);
            try {
                const res = await api.getShops(1, 1000);
                const list = Array.isArray(res) ? res : res.list;
                const mapped: Canteen[] = (list || []).map((s: any) => ({
                    id: String(s.id),
                    name: s.name,
                    address: s.address || '',
                    managerName: s.manager || '',
                    phone: s.phone || '',
                    status: 'ACTIVE',
                    rating: s.rating,
                    monthlySales: s.monthlySales ?? s.monthly_sales,
                    logo: s.logo,
                }));
                setCanteens(mapped);
            } catch (e: any) {
                setError(e.message || '加载商铺失败');
            } finally {
                setLoading(false);
            }
        };
        load();
    }, []);

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
                <h2 className={styles.title}>商铺管理</h2>
                <button className={styles.addButton}>
                    <Plus size={18} /> 新增商铺
                </button>
            </div>

            <div className={styles.tableWrapper}>
                {loading && <div style={{ padding: '0.5rem 1rem', color: '#64748b' }}>加载中...</div>}
                {error && <div style={{ padding: '0.5rem 1rem', color: '#b91c1c' }}>错误：{error}</div>}
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>商铺</th>
                            <th>负责人</th>
                            <th>联系电话</th>
                            <th>评分</th>
                            <th>月售</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        {canteens.map(canteen => (
                            <tr key={canteen.id}>
                                <td>
                                    <div className={styles.canteenInfo}>
                                        {canteen.logo ? (
                                            <img src={canteen.logo} alt={canteen.name} style={{ width: 40, height: 40, borderRadius: 6, objectFit: 'cover', marginRight: 8 }} />
                                        ) : null}
                                        <div style={{ display: 'flex', flexDirection: 'column' }}>
                                            <span className={styles.canteenName}>{canteen.name}</span>
                                            <span className={styles.canteenAddress}>{canteen.address || '—'}</span>
                                        </div>
                                    </div>
                                </td>
                                <td>{canteen.managerName || '—'}</td>
                                <td>{canteen.phone || '—'}</td>
                                <td>⭐ {canteen.rating ?? '-'}</td>
                                <td>{canteen.monthlySales ?? '-'}</td>
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
                            <h3 className={styles.modalTitle}>账号管理 - {activeCanteen.name}</h3>
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
                                    该商铺暂无账号
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
                            <h3 className={styles.modalTitle}>编辑商铺信息</h3>
                            <button className={styles.closeBtn} onClick={closeModal}>
                                <X size={20} />
                            </button>
                        </div>
                        <div className={styles.modalContent}>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        商铺名称
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
