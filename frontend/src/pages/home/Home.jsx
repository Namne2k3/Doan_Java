import React, { useState } from 'react'
import "./home.css"
import Header from '../../components/Header/Header'
import Menu from '../../components/Menu/Menu'
import TechDisplay from '../../components/TechDisplay/TechDisplay'
const Home = () => {

    const [category, setCategory] = useState("All")

    return (
        <div>
            <Header />
            <Menu category={category} setCategory={setCategory} />
            <TechDisplay category={category} />
        </div>
    )
}

export default Home