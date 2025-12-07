'use client';

import { useState } from 'react';
import { Search, Ban, CheckCircle } from 'lucide-react';
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

    const toggleStatus = (id: string) => {
        setUsers(prev => prev.map(u =>
            u.id === id ? { ...u, status: u.status === 'NORMAL' ? 'BANNED' : 'NORMAL' } : u
        ));
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
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
