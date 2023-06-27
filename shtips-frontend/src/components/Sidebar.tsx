import React, {useState, useEffect} from "react";
import axios from "axios";
import "./Sidebar.css";

function Sidebar() {
	const [users, setUsers] = useState([]);

	useEffect(() => {
		fetchUsers();
	}, []);

	const fetchUsers = async () => {
		// try {
		// 	const response = await axios.get("/api/users/search");
		// 	setUsers(response.data);
		// } catch (error) {
		// 	console.error("Error fetching users:", error);
		// }
	};

	return (
		<div className="sidebar">
			{users.map((user) => (
				<div key={user.id} className="sidebar__user">
					<h3>{user.username}</h3>
					{/* Show unread message indicator if needed */}
				</div>
			))}
		</div>
	);
}

export default Sidebar;
