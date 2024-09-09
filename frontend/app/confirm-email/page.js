'use client'

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import api from '@/utils/api';

export default function ConfirmEmail() {
  const [status, setStatus] = useState('Confirming your email...');
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('token');
    if (token) {
      confirmEmail(token);
    } else {
      setStatus('Invalid confirmation link');
    }
  }, []);

  const confirmEmail = async (token) => {
    try {
      const response = await api.get(`/users/confirm-email?token=${token}`);
      setStatus(response.data);
      if (response.data.includes("successfully")) {
        setTimeout(() => router.push('/login'), 3000);
      }
    } catch (error) {
      const errorMessage = error.response?.data || 'Failed to confirm email';
      setStatus(errorMessage);
      if (errorMessage === "Email already confirmed") {
        setTimeout(() => router.push('/login'), 3000);
      }
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-gray-800 rounded-lg shadow-xl">
      <h1 className="text-2xl font-bold mb-4 text-center text-white">Email Confirmation</h1>
      <p className="text-center text-gray-300">{status}</p>
      {status.includes("expired") && (
        <button 
          onClick={() => router.push('/resend-confirmation')} 
          className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          Resend Confirmation Email
        </button>
      )}
    </div>
  );
}