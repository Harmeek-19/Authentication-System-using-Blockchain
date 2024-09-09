'use client'

import { useState, useEffect } from 'react';
import api from '@/utils/api';

export default function BlockchainStatus() {
  const [statusData, setStatusData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api.get('/blockchain/status');
        setStatusData(response.data);
      } catch (err) {
        setError(err.response?.data || 'An error occurred while fetching blockchain status');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
    const interval = setInterval(fetchData, 30000);
    return () => clearInterval(interval);
  }, []);

  if (isLoading) return <div className="text-center">Loading...</div>;
  if (error) return <div className="text-red-500 text-center">{error}</div>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-primary to-accent">Blockchain Status</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {statusData && statusData.map((block, index) => (
          <div key={index} className="bg-gray-800 p-6 rounded-lg shadow-lg transform hover:scale-105 transition-transform duration-200">
            <h2 className="text-xl font-semibold mb-2 text-secondary">Block {index + 1}</h2>
            <p className="text-sm text-white">Hash: {block.hash.substring(0, 15)}...</p>
            <p className="text-sm text-white">Transactions: {block.transactions.length}</p>
          </div>
        ))}
      </div>
    </div>
  );
}