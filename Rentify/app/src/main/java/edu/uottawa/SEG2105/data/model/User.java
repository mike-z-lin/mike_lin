package edu.uottawa.SEG2105.data.model;

import androidx.annotation.NonNull;

public class User {
        public String id, email, password, role, name;
		public boolean disabled;
		
        public User(){
        }

		public User(String i, String n,  String p,  String e,  String r ) {
			id = i;
			name = n;
			email = e;
			password = p;
			role = r;
			disabled = false;
		}
		
        public String toString() {
            return name+ ":" + role;
        }


}
