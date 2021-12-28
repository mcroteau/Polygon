package shape.repo;

import shape.model.User;
import shape.model.UserPermission;
import shape.model.UserRole;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataStore
public class UserRepo {

	@Inject
	Qio qio;

	public User getSaved() {
		String idSql = "select max(id) from users";
		long id = qio.getLong(idSql, new Object[]{});
		return get(id);
	}

	public long getId() {
		String sql = "select max(id) from users";
		long id = qio.getLong(sql, new Object[]{});
		return id;
	}

	public long getCount() {
		String sql = "select count(*) from users";
		Long count = qio.getLong(sql, new Object[] { });
		return count;
	}

	public User get(long id) {
		String sql = "select * from users where id = [+]";
		User user = (User) qio.get(sql, new Object[] { id }, User.class);

		if(user == null) user = new User();
		return user;
	}

	public User getPhone(String username) {
		String sql = "select * from users where phone = '[+]'";
		User user = (User) qio.get(sql, new Object[] { username }, User.class);
		return user;
	}

	public List<User> getList() {
		String sql = "select * from users";
		List<User> users = (ArrayList) qio.getList(sql, new Object[]{}, User.class);
		return users;
	}

	public Boolean save(User user) {
		String sql = "insert into users (name, phone, password, date_created) values ('[+]', '[+]', '[+]', [+])";
		qio.save(sql, new Object[]{
				user.getName(),
				user.getPhone(),
				user.getPassword(),
				user.getDateCreated()
		});
		return true;
	}

	public boolean update(User user) {
		String sql = "update users set name = '[+]', phone = '[+]', username = '[+]', password = '[+]' where id = [+]";
		qio.update(sql, new Object[]{
				user.getName(),
				user.getPhone(),
				user.getUsername(),
				user.getPassword(),
				user.getId()
		});
		return true;
	}

	public boolean updatePassword(User user) {
		String sql = "update users set password = '[+]' where id = [+]";
		qio.update(sql, new Object[]{
				user.getPassword(),
				user.getId()
		});
		return true;
	}

	public User getReset(String username, String uuid){
		String sql = "select * from users where username = '[+]' and uuid = '[+]'";
		User user = (User) qio.get(sql, new Object[] { username, uuid }, User.class);
		return user;
	}

	public boolean delete(long id) {
		String sql = "delete from users where id = [+]";
		qio.update(sql, new Object[] { id });
		return true;
	}

	public String getUserPassword(String phone) {
		User user = getPhone(phone);
		return user.getPassword();
	}

	public boolean saveUserRole(long userId, long roleId){
		String sql = "insert into user_roles (role_id, user_id) values ([+], [+])";
		qio.save(sql, new Object[]{roleId, userId});
		return true;
	}

	public boolean savePermission(long accountId, String permission){
		String sql = "insert into user_permissions (user_id, permission) values ([+], '[+]')";
		qio.save(sql, new Object[]{ accountId,  permission });
		return true;
	}

	public Set<String> getUserRoles(long id) {
		String sql = "select r.name as name from user_roles ur inner join roles r on r.id = ur.role_id where ur.user_id = [+]";
		List<UserRole> rolesList = (ArrayList) qio.getList(sql, new Object[]{ id }, UserRole.class);
		Set<String> roles = new HashSet<>();
		for(UserRole role: rolesList){
			roles.add(role.getName());
		}
		return roles;
	}

	public Set<String> getUserPermissions(long id) {
		String sql = "select permission from user_permissions where user_id = [+]";
		List<UserPermission> permissionsList = (ArrayList) qio.getList(sql, new Object[]{ id }, UserPermission.class);
		Set<String> permissions = new HashSet<>();
		for(UserPermission permission: permissionsList){
			permissions.add(permission.getPermission());
		}
		return permissions;
	}

}