'use client';

import { useState, useEffect } from 'react';
import { Plus, Edit2, Trash2, Power, Store, X, Users } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';
import AddMerchantModal from './components/AddMerchantModal';
import AccountListModal from './components/AccountListModal';

interface Canteen {
    id: string;
    name: string;
    address: string;
    campus: string;
    status: number; // 1: Active, 0: Inactive
    imageUrl?: string;
}

export default function CanteensPage() {
    const [canteens, setCanteens] = useState<Canteen[]>([]);
    const [loading, setLoading] = useState(true);
    const [editingCanteen, setEditingCanteen] = useState<Canteen | null>(null);
    const [editForm, setEditForm] = useState({ name: '', campus: '', address: '', imageUrl: '' });

    // Modals state
    const [showAddModal, setShowAddModal] = useState(false);
    const [viewAccountsCanteen, setViewAccountsCanteen] = useState<{ id: string, name: string } | null>(null);

    const fetchCanteens = async () => {
        setLoading(true);
        try {
            const res = await api.admin.getCanteens();
            if (res.success) {
                const mapped = res.data.map((c: any) => ({
                    id: c.canteenId.toString(),
                    name: c.name,
                    campus: c.campus,
                    address: c.location || '',
                    status: c.status,
                    imageUrl: c.imageUrl
                }));
                setCanteens(mapped);
            }
        } catch (err) {
            console.error('Failed to fetch canteens', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCanteens();
    }, []);

    const toggleStatus = async (canteen: Canteen) => {
        const newStatus = canteen.status === 1 ? 0 : 1;
        try {
            const res = await api.admin.updateCanteen(canteen.id, { ...canteen, status: newStatus });
            if (res.success) {
                fetchCanteens();
            }
        } catch (err) {
            alert('操作失败');
        }
    };

    const handleEditCanteen = (canteen: Canteen) => {
        setEditingCanteen(canteen);
        setEditForm({
            name: canteen.name,
            campus: canteen.campus,
            address: canteen.address,
            imageUrl: canteen.imageUrl || ''
        });
    };

    const saveCanteenEdit = async () => {
        if (!editingCanteen) return;
        try {
            const res = await api.admin.updateCanteen(editingCanteen.id, {
                ...editingCanteen,
                name: editForm.name,
                campus: editForm.campus,
                location: editForm.address,
                imageUrl: editForm.imageUrl
            });
            if (res.success) {
                fetchCanteens();
                setEditingCanteen(null);
            }
        } catch (err) {
            alert('保存失败');
        }
    };

    const closeModal = () => {
        setEditingCanteen(null);
    };

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>商家管理</h2>
                <button className={styles.addButton} onClick={() => setShowAddModal(true)}>
                    <Plus size={18} /> 新增商家
                </button>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
            ) : (
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>商家名称/院区</th>
                                <th>详细地址</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {canteens.length === 0 ? (
                                <tr>
                                    <td colSpan={4} style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>
                                        暂无商家数据
                                    </td>
                                </tr>
                            ) : (
                                canteens.map(canteen => (
                                    <tr key={canteen.id}>
                                        <td>
                                            <div className={styles.canteenInfo}>
                                                <span className={styles.canteenName}>{canteen.name}</span>
                                                <span className={styles.canteenAddress}>{canteen.campus}</span>
                                            </div>
                                        </td>
                                        <td>{canteen.address}</td>
                                        <td>
                                            <span className={`${styles.statusBadge} ${canteen.status === 1 ? styles.statusActive : styles.statusInactive}`}>
                                                {canteen.status === 1 ? '营业中' : '已停业'}
                                            </span>
                                        </td>
                                        <td>
                                            <button
                                                className={styles.actionBtn}
                                                onClick={() => toggleStatus(canteen)}
                                                title={canteen.status === 1 ? "停业" : "启用"}
                                            >
                                                <Power size={14} /> {canteen.status === 1 ? "停业" : "启用"}
                                            </button>
                                            <button
                                                className={styles.actionBtn}
                                                onClick={() => handleEditCanteen(canteen)}
                                            >
                                                <Edit2 size={14} /> 编辑
                                            </button>
                                            <button
                                                className={styles.actionBtn}
                                                onClick={() => setViewAccountsCanteen({ id: canteen.id, name: canteen.name })}
                                            >
                                                <Users size={14} /> 账号
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            )}

            {/* Modals */}
            {showAddModal && (
                <AddMerchantModal
                    onClose={() => setShowAddModal(false)}
                    onSuccess={fetchCanteens}
                />
            )}

            {viewAccountsCanteen && (
                <AccountListModal
                    canteenId={viewAccountsCanteen.id}
                    canteenName={viewAccountsCanteen.name}
                    onClose={() => setViewAccountsCanteen(null)}
                />
            )}

            {/* Edit Canteen Modal */}
            {editingCanteen && (
                <div className={styles.modalOverlay} onClick={(e) => {
                    if (e.target === e.currentTarget) closeModal();
                }}>
                    <div className={styles.modal} style={{ width: '450px' }}>
                        <div className={styles.modalHeader}>
                            <h3 className={styles.modalTitle}>编辑商家信息</h3>
                            <button className={styles.closeBtn} onClick={closeModal}>
                                <X size={20} />
                            </button>
                        </div>
                        <div className={styles.modalContent}>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                <div>
                                    <label className={styles.label}>
                                        商家名称
                                    </label>
                                    <input
                                        type="text"
                                        value={editForm.name}
                                        onChange={e => setEditForm({ ...editForm, name: e.target.value })}
                                        className={styles.input}
                                    />
                                </div>
                                <div>
                                    <label className={styles.label}>
                                        所属院区
                                    </label>
                                    <select
                                        className={styles.input}
                                        value={editForm.campus}
                                        onChange={e => setEditForm({ ...editForm, campus: e.target.value })}
                                    >
                                        <option value="南校区">南校区</option>
                                        <option value="北校区">北校区</option>
                                    </select>
                                </div>
                                <div>
                                    <label className={styles.label}>
                                        详细地址
                                    </label>
                                    <input
                                        type="text"
                                        className={styles.input}
                                        value={editForm.address}
                                        onChange={e => setEditForm({ ...editForm, address: e.target.value })}
                                    />
                                </div>
                                <div>
                                    <label className={styles.label}>
                                        封面图 URL
                                    </label>
                                    <input
                                        type="text"
                                        className={styles.input}
                                        value={editForm.imageUrl}
                                        onChange={e => setEditForm({ ...editForm, imageUrl: e.target.value })}
                                        placeholder="https://..."
                                    />
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.8rem', marginTop: '1rem' }}>
                                    <button
                                        onClick={closeModal}
                                        className={styles.secondaryBtn}
                                    >
                                        取消
                                    </button>
                                    <button
                                        onClick={saveCanteenEdit}
                                        className={styles.primaryBtn}
                                    >
                                        保存修改
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
