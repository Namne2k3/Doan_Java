import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { userService } from './services';
import LoginPage from './pages/auth/Login';
import ProfilePage from './pages/user/ProfilePage';
import RegistrationPage from './pages/auth/Register';
import UserManagementPage from './pages/user/UserManagementPage';
import UpdateUser from './pages/user/UpdateUser';
import Navbar from './components/Navbar/Navbar';
import Home from './pages/home/Home';
import Cart from './pages/cart/Cart';
import PlaceOrder from './pages/placeorder/PlaceOrder';
import Footer from "./components/Footer/Footer";
import { useState } from "react";
import LoginPopup from "./components/LoginPopup/LoginPopup";
import { useContext } from "react";
import { StoreContext } from "./context/StoreContext";
import { useEffect } from "react";

function App() {

  const [showLogin, setShowLogin] = useState(false);
  const { fetchProfileData } = useContext(StoreContext)

  useEffect(() => {

  }, [])

  return (
    <BrowserRouter>
      <>
        {
          showLogin
            ?
            <LoginPopup setShowLogin={setShowLogin} />
            :
            <></>
        }
        <div className="app">
          <Navbar setShowLogin={setShowLogin} />
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/cart" element={<Cart />} />
            <Route exact path="/order" element={<PlaceOrder />} />
            <Route exact path="/login" element={<LoginPage />} />
            <Route path="/profile" element={<ProfilePage />} />

            {/* Check if user is authenticated and admin before rendering admin-only routes */}
            {userService.adminOnly() && (
              <>
                <Route path="/register" element={<RegistrationPage />} />
                <Route path="/admin/user-management" element={<UserManagementPage />} />
                <Route path="/update-user/:userId" element={<UpdateUser />} />
              </>
            )}

            <Route path="*" element={<Navigate to="/login" />} />‰
          </Routes>
        </div>
        <Footer />
      </>
    </BrowserRouter>
  );
}

export default App;
