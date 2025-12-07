'use client';

import { useState } from 'react';
import { Search, Ban, CheckCircle, Edit2, X } from 'lucide-react';
import styles from './page.module.css';

interface User {
    id: string;
    name: string;
    studentId: string;
    phone: string;
    status: 'NORMAL' | 'BANNED';
    registerDate: string;
}

const initialUsers: User[] = [
    { id: '1', name: '张三', studentId: '31901001', phone: '15800001234', status: 'NORMAL', registerDate: '2025-09-01' },
    { id: '2', name: '李四', studentId: '31901002', phone: '15800002345', status: 'NORMAL', registerDate: '2025-09-02' },
    { id: '3', name: '王五', studentId: '31901003', phone: '15800003456', status: 'BANNED', registerDate: '2025-09-03' },
    { id: '4', name: '赵六', studentId: '31901004', phone: '15800004567', status: 'NORMAL', registerDate: '2025-09-04' },
    { id: '5', name: '孙七', studentId: '31901005', phone: '15800005678', status: 'NORMAL', registerDate: '2025-09-05' },
    { id: '6', name: '周八', studentId: '31901006', phone: '15800006789', status: 'BANNED', registerDate: '2025-09-06' },
];

export default function UsersPage() {
    const [users, setUsers] = useState<User[]>(initialUsers);
    const [searchTerm, setSearchTerm] = useState('');
    const [editingUser, setEditingUser] = useState<User | null>(null);
    const [editForm, setEditForm] = useState({ phone: '' });

    const toggleStatus = (id: string) => {
        setUsers(prev => prev.map(u =>
            u.id === id ? { ...u, status: u.status === 'NORMAL' ? 'BANNED' : 'NORMAL' } : u
        ));
    };

    const handleEditUser = (user: User) => {
        setEditingUser(user);
        setEditForm({ phone: user.phone });
    };

    const saveUserEdit = () => {
        if (!editingUser) return;
        setUsers(prev => prev.map(u =>
            u.id === editingUser.id ? { ...u, phone: editForm.phone } : u
        ));
        setEditingUser(null);
    };

    const closeModal = () => {
        setEditingUser(null);
    };

    const filteredUsers = users.filter(user =>
        user.name.includes(searchTerm) ||
        user.studentId.includes(searchTerm) ||
        user.phone.includes(searchTerm)
    );

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>用户管理</h2>
                <div className={styles.searchBox}>
                    <Search size={18} className={styles.searchIcon} />
                    <input
                        type="text"
                        placeholder="搜索姓名/学号/手机号"
                        className={styles.searchInput}
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            <div className={styles.tableWrapper}>
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>用户</th>
                            <th>学号</th>
                            <th>手机号</th>
                            <th>注册时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredUsers.length === 0 ? (
                            <tr>
                                <td colSpan={6} style={{ textAlign: 'center', padding: '2rem', color: 'var(--muted-foreground)' }}>
                                    暂无相关用户
                                </td>
                            </tr>
                        ) : (
                            filteredUsers.map(user => (
                                <tr key={user.id}>
                                    <td>
                                        <div className={styles.userInfo}>
                                            <div className={styles.userAvatar}>
                                                {user.name[0]}
                                            </div>
                                            <span className={styles.userName}>{user.name}</span>
                                        </div>
                                    </td>
                                    <td>{user.studentId}</td>
                                    <td>{user.phone}</td>
                                    <td>{user.registerDate}</td>
                                    <td>
                                        <span className={`${styles.statusBadge} ${user.status === 'NORMAL' ? styles.statusNormal : styles.statusBanned}`}>
                                            {user.status === 'NORMAL' ? '正常' : '封禁中'}
                                        </span>
                                    </td>
                                    <td>
                                        {user.status === 'NORMAL' ? (
                                            <button
                                                className={`${styles.actionBtn} ${styles.banBtn}`}
                                                onClick={() => toggleStatus(user.id)}
                                                title="封禁用户"
                                            >
                                                <Ban size={14} style={{ marginRight: '4px' }} /> 封禁
                                            </button>
                                        ) : (
                                            <button
                                                className={styles.actionBtn}
                                                onClick={() => toggleStatus(user.id)}
                                                title="解封用户"
                                                style={{ color: 'var(--success-text)', borderColor: 'var(--success)' }}
                                            >
                                                <CheckCircle size={14} style={{ marginRight: '4px' }} /> 解封
                                            </button>
                                        )}
                                        <button
                                            className={styles.actionBtn}
                                            onClick={() => handleEditUser(user)}
                                            style={{ marginLeft: '0.5rem' }}
                                        >
                                            <Edit2 size={14} style={{ marginRight: '4px' }} /> 编辑
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Edit User Modal */}
            {editingUser && (
                <div className={styles.modalOverlay} onClick={(e) => {
                    if (e.target === e.currentTarget) closeModal();
                }}>
                    <div className={styles.modal} style={{ width: '400px' }}>
                        <div className={styles.modalHeader}>
                            <h3 className={styles.modalTitle}>编辑用户信息</h3>
                            <button className={styles.closeBtn} onClick={closeModal}>
                                <X size={20} />
                            </button>
                        </div>
                        <div className={styles.modalContent}>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        姓名
                                    </label>
                                    <input
                                        type="text"
                                        value={editingUser.name}
                                        disabled
                                        style={{ width: '100%', padding: '0.5rem', borderRadius: '0.375rem', border: '1px solid var(--border)', background: 'var(--muted)' }}
                                    />
                                </div>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        学号
                                    </label>
                                    <input
                                        type="text"
                                        value={editingUser.studentId}
                                        disabled
                                        style={{ width: '100%', padding: '0.5rem', borderRadius: '0.375rem', border: '1px solid var(--border)', background: 'var(--muted)' }}
                                    />
                                </div>
                                <div>
                                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', fontWeight: 500 }}>
                                        手机号
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
                                        onClick={saveUserEdit}
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
