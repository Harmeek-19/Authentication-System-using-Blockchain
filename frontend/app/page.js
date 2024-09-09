export default function Home() {
  return (
    <div className="text-center">
      <h1 className="text-5xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-primary to-accent">
        Welcome to BlockchainX
      </h1>
      <p className="text-xl mb-8">Explore the world of blockchain with our cutting-edge platform.</p>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {['Security', 'Transparency', 'Efficiency'].map((feature) => (
          <div key={feature} className="bg-gray-800 p-6 rounded-lg shadow-lg transform hover:scale-105 transition-transform duration-200">
            <h2 className="text-2xl font-semibold mb-4 text-secondary">{feature}</h2>
            <p>Experience unparalleled {feature.toLowerCase()} with our blockchain solutions.</p>
          </div>
        ))}
      </div>
    </div>
  )
}