'use client'

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import api from '@/utils/api';

export default function Login() {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');
    setSuccess('');

    try {
      const response = await api.post('/users/login', formData);
      localStorage.setItem('token', response.data.token);
      setSuccess('Login successful! Redirecting to dashboard...');
      setTimeout(() => router.push('/dashboard'), 2000);
    } catch (err) {
      if (err.response?.data === "Please confirm your email before logging in") {
        setError("Please check your email and confirm your account before logging in.");
      } else {
        setError(err.response?.data || 'An error occurred during login');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto bg-gray-800 p-8 rounded-lg shadow-lg">
      <h1 className="text-3xl font-bold mb-6 text-center bg-clip-text text-transparent bg-gradient-to-r from-primary to-accent">Login</h1>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      {success && <p className="text-green-500 mb-4">{success}</p>}
      <form onSubmit={handleSubmit} className="space-y-6">
        {['Username', 'Password'].map((field) => (
          <div key={field}>
            <label htmlFor={field.toLowerCase()} className="block mb-2 text-sm font-medium text-gray-300">{field}</label>
            <input 
              type={field === 'Password' ? 'password' : 'text'} 
              id={field.toLowerCase()} 
              name={field.toLowerCase()}
              value={formData[field.toLowerCase()]}
              onChange={handleChange}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-primary text-white"
              required
            />
          </div>
        ))}
        <button 
          type="submit" 
          className="w-full bg-gradient-to-r from-primary to-accent text-white py-2 rounded-md hover:opacity-90 transition-opacity duration-200"
          disabled={isLoading}
        >
          {isLoading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
}