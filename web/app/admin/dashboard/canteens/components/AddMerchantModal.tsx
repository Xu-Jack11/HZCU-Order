'use client';

import { useState } from 'react';
import { X, Loader2 } from 'lucide-react';
import styles from '../page.module.css';
import { api } from '@/lib/api';

interface AddMerchantModalProps {
    onClose: () => void;
    onSuccess: () => void;
}

export default function AddMerchantModal({ onClose, onSuccess }: AddMerchantModalProps) {
    const [loading, setLoading] = useState(false);
    const [form, setForm] = useState({
        name: '',
        campus: '南校区',
        location: '',
        contactPhone: '',
        businessHours: '08:00-20:00',
        serviceFeeRate: 0.1,
        remark: '',
        imageUrl: '',
        username: '',
        password: '',
        realName: '',
        mobile: ''
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await api.admin.createMerchant(form);
            if (res.success) {
                onSuccess();
                onClose();
            }
        } catch (err: any) {
            alert(err.message || '创建失败');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.modalOverlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
            <div className={styles.modal} style={{ width: '600px', maxHeight: '90vh', overflowY: 'auto' }}>
                <div className={styles.modalHeader}>
                    <h3 className={styles.modalTitle}>新增商家及主账号</h3>
                    <button className={styles.closeBtn} onClick={onClose}>
                        <X size={20} />
                    </button>
                </div>
                <form onSubmit={handleSubmit} className={styles.modalContent}>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
                        {/* Canteen Info */}
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            <h4 style={{ margin: '0 0 0.5rem 0', color: '#1e293b', borderBottom: '1px solid #e2e8f0', paddingBottom: '0.5rem' }}>商家信息</h4>
                            <div>
                                <label className={styles.label}>商家名称</label>
                                <input
                                    required
                                    type="text"
                                    className={styles.input}
                                    value={form.name}
                                    onChange={e => setForm({ ...form, name: e.target.value })}
                                    placeholder="例如：第一食堂3号档口"
                                />
                            </div>
                            <div>
                                <label className={styles.label}>所属院区</label>
                                <select
                                    className={styles.input}
                                    value={form.campus}
                                    onChange={e => setForm({ ...form, campus: e.target.value })}
                                >
                                    <option value="南校区">南校区</option>
                                    <option value="北校区">北校区</option>
                                </select>
                            </div>
                            <div>
                                <label className={styles.label}>详细地址</label>
                                <input
                                    required
                                    type="text"
                                    className={styles.input}
                                    value={form.location}
                                    onChange={e => setForm({ ...form, location: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className={styles.label}>服务费率 (0.00 - 1.00)</label>
                                <input
                                    required
                                    type="number"
                                    step="0.01"
                                    className={styles.input}
                                    value={form.serviceFeeRate}
                                    onChange={e => setForm({ ...form, serviceFeeRate: parseFloat(e.target.value) })}
                                />
                            </div>
                        </div>

                        {/* Account Info */}
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            <h4 style={{ margin: '0 0 0.5rem 0', color: '#1e293b', borderBottom: '1px solid #e2e8f0', paddingBottom: '0.5rem' }}>管理员账号</h4>
                            <div>
                                <label className={styles.label}>登录用户名</label>
                                <input
                                    required
                                    type="text"
                                    className={styles.input}
                                    value={form.username}
                                    onChange={e => setForm({ ...form, username: e.target.value })}
                                    placeholder="用于登录后台"
                                />
                            </div>
                            <div>
                                <label className={styles.label}>登录密码</label>
                                <input
                                    required
                                    type="password"
                                    className={styles.input}
                                    value={form.password}
                                    onChange={e => setForm({ ...form, password: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className={styles.label}>真实姓名</label>
                                <input
                                    required
                                    type="text"
                                    className={styles.input}
                                    value={form.realName}
                                    onChange={e => setForm({ ...form, realName: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className={styles.label}>手机号码</label>
                                <input
                                    required
                                    type="tel"
                                    className={styles.input}
                                    value={form.mobile}
                                    onChange={e => setForm({ ...form, mobile: e.target.value })}
                                />
                            </div>
                        </div>
                    </div>

                    <div style={{ marginTop: '1.5rem' }}>
                        <label className={styles.label}>封面图 URL</label>
                        <input
                            type="text"
                            className={styles.input}
                            value={form.imageUrl}
                            onChange={e => setForm({ ...form, imageUrl: e.target.value })}
                            placeholder="https://..."
                        />
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem', marginTop: '2rem' }}>
                        <button type="button" onClick={onClose} className={styles.secondaryBtn} disabled={loading}>
                            取消
                        </button>
                        <button type="submit" className={styles.primaryBtn} disabled={loading} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                            {loading && <Loader2 size={16} className={styles.spin} />}
                            确认创建
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
