import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import LoginPage from '../page';
import '@testing-library/jest-dom';

// Mock useRouter
const mockPush = jest.fn();
jest.mock('next/navigation', () => ({
    useRouter: () => ({
        push: mockPush,
    }),
}));

describe('LoginPage White-box Tests', () => {
    beforeEach(() => {
        mockPush.mockClear();
    });

    it('renders login form correctly', () => {
        render(<LoginPage />);
        expect(screen.getByLabelText('账号')).toBeInTheDocument();
        expect(screen.getByLabelText('密码')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: '登 录' })).toBeInTheDocument();
    });

    it('handles form submission correctly', async () => {
        render(<LoginPage />);
        
        const submitButton = screen.getByRole('button', { name: '登 录' });
        const form = submitButton.closest('form');
        fireEvent.submit(form!);

        // Check loading state
        await waitFor(() => {
            expect(screen.getByRole('button')).toHaveTextContent('登录中...');
        });
        expect(screen.getByRole('button')).toBeDisabled();

        // Check navigation after delay
        await waitFor(() => {
            expect(mockPush).toHaveBeenCalledWith('/dashboard');
        }, { timeout: 2000 });
    });
});
