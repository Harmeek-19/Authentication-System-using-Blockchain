'use client'

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import api from '@/utils/api';

export default function Dashboard() {
  const [userData, setUserData] = useState(null);
  const [publicKey, setPublicKey] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const router = useRouter();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [dashboardResponse, publicKeyResponse] = await Promise.all([
          api.get('/users/dashboard'),
          api.get('/users/public-key')
        ]);
        setUserData(dashboardResponse.data);
        setPublicKey(publicKeyResponse.data);
      } catch (err) {
        setError('Failed to fetch user data. Please try logging in again.');
        localStorage.removeItem('token');
        setTimeout(() => router.push('/login'), 3000);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [router]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    router.push('/login');
  };

  if (isLoading) return <div className="text-center">Loading...</div>;
  if (error) return <div className="text-red-500 text-center">{error}</div>;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-primary to-accent">Dashboard</h1>
      {userData && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-semibold mb-4 text-secondary">Account Information</h2>
            <p className="text-white">Username: {userData.username}</p>
            <p className="text-white">Email: {userData.email}</p>
          </div>
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-semibold mb-4 text-secondary">Account Balance</h2>
            <p className="text-4xl font-bold text-white">{userData.balance || 0} COIN</p>
          </div>
        </div>
      )}
      {publicKey && (
        <div className="mt-6 bg-gray-800 p-6 rounded-lg shadow-lg">
          <h2 className="text-2xl font-semibold mb-4 text-secondary">Public Key</h2>
          <p className="text-white break-all">{publicKey}</p>
        </div>
      )}
      <div className="mt-8">
        <h2 className="text-2xl font-semibold mb-4 text-secondary">Recent Transactions</h2>
        {userData?.recentTransactions?.length > 0 ? (
          <ul className="space-y-2">
            {userData.recentTransactions.map((transaction, index) => (
              <li key={index} className="bg-gray-700 p-4 rounded-lg flex justify-between items-center">
                <span>{transaction.type} {Math.abs(transaction.amount)} COIN</span>
                <span className={transaction.amount > 0 ? 'text-green-500' : 'text-red-500'}>
                  {transaction.amount > 0 ? '+' : '-'}{Math.abs(transaction.amount)} COIN
                </span>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-400">No recent transactions</p>
        )}
      </div>
      <div className="mt-8 flex justify-between">
        <button 
          onClick={() => router.push('/blockchain-status')}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          View Blockchain Status
        </button>
        <button 
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
        >
          Logout
        </button>
      </div>
    </div>
  );
}