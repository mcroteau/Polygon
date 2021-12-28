package shape.repo;

import shape.model.Role;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;


import java.util.ArrayList;
import java.util.List;

@DataStore
public class RoleRepo {

	@Inject
	Qio qio;


	public int count() {
		String sql = "select count(*) from roles";
		int count = qio.getInteger(sql, new Object[] { });
		return count;
	}

	public Role get(int id) {
		String sql = "select * from roles where id = [+]";
		Role role = (Role) qio.get(sql, new Object[] { id },Role.class);
		return role;
	}

	public Role get(String name) {
		String sql = "select * from roles where name = '[+]'";
		Role role = (Role) qio.get(sql, new Object[] { name }, Role.class);
		return role;
	}

	public List<Role> findAll() {
		String sql = "select * from roles";
		List<Role> roles = (ArrayList) qio.getList(sql, new Object[]{}, Role.class);
		return roles;
	}

	public void save(Role role) {
		String sql = "insert into roles (name) values('[+]')";
		qio.save(sql, new Object[]{
				role.getName()
		});
	}

}