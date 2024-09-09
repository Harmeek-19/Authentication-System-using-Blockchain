'use client'

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

export default function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const response = await axios.post('http://localhost:8080/api/users/register', formData);
      console.log(response.data);
      router.push('/login'); // Redirect to login page after successful registration
    } catch (err) {
      setError(err.response?.data || 'An error occurred during registration');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto bg-gray-800 p-8 rounded-lg shadow-lg">
      <h1 className="text-3xl font-bold mb-6 text-center bg-clip-text text-transparent bg-gradient-to-r from-primary to-accent">Register</h1>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-6">
        {['Username', 'Email', 'Password'].map((field) => (
          <div key={field}>
            <label htmlFor={field.toLowerCase()} className="block mb-2 text-sm font-medium text-gray-300">{field}</label>
            <input 
              type={field === 'Password' ? 'password' : field === 'Email' ? 'email' : 'text'} 
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
          {isLoading ? 'Registering...' : 'Register'}
        </button>
      </form>
    </div>
  );
}