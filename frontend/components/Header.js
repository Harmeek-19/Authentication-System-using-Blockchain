'use client'
import Link from 'next/link'
import { useState, useEffect } from 'react'
import { logout } from '@/utils/api'

export default function Header() {
  const [isOpen, setIsOpen] = useState(false)
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  useEffect(() => {
    setIsLoggedIn(!!localStorage.getItem('token'))
  }, [])

  const handleLogout = () => {
    logout()
    setIsLoggedIn(false)
  }

  return (
    <header className="bg-gradient-to-r from-primary to-accent text-white shadow-lg">
      <nav className="container mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex items-center justify-between">
          <Link href="/" className="text-2xl font-bold">
            <span className="bg-clip-text text-transparent bg-gradient-to-r from-secondary to-accent">
              BlockchainX
            </span>
          </Link>
          <div className="hidden md:flex space-x-4">
            <Link href="/" className="hover:text-secondary transition-colors duration-200">Home</Link>
            {isLoggedIn ? (
              <>
                <Link href="/dashboard" className="hover:text-secondary transition-colors duration-200">Dashboard</Link>
                <Link href="/blockchain-status" className="hover:text-secondary transition-colors duration-200">Blockchain Status</Link>
                <button onClick={handleLogout} className="hover:text-secondary transition-colors duration-200">Logout</button>
              </>
            ) : (
              <>
                <Link href="/register" className="hover:text-secondary transition-colors duration-200">Register</Link>
                <Link href="/login" className="hover:text-secondary transition-colors duration-200">Login</Link>
              </>
            )}
          </div>
          <button
            className="md:hidden text-white focus:outline-none"
            onClick={() => setIsOpen(!isOpen)}
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>
        {isOpen && (
          <div className="mt-4 md:hidden">
            <Link href="/" className="block py-2 hover:text-secondary transition-colors duration-200">Home</Link>
            {isLoggedIn ? (
              <>
                <Link href="/dashboard" className="block py-2 hover:text-secondary transition-colors duration-200">Dashboard</Link>
                <Link href="/blockchain-status" className="block py-2 hover:text-secondary transition-colors duration-200">Blockchain Status</Link>
                <button onClick={handleLogout} className="block py-2 hover:text-secondary transition-colors duration-200">Logout</button>
              </>
            ) : (
              <>
                <Link href="/register" className="block py-2 hover:text-secondary transition-colors duration-200">Register</Link>
                <Link href="/login" className="block py-2 hover:text-secondary transition-colors duration-200">Login</Link>
              </>
            )}
          </div>
        )}
      </nav>
    </header>
  )
}