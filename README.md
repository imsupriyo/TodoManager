# Todo Manager:

A full CRUD based Spring MVC application with user authentication and role-based
access control.
User with role admin has additional privilege to manage users and update all Todos.

Unit test and Integration Test have been done utilizing JUnit and Mockito.

# **ER Diagram:**

![**ER Diagram**](entity_relationship.png)

# Snapshots:

## **Todo Controller:**

### Login Page:

![**Login Page**](login_page.png)

### Homepage for Employee:

User with Employee role only has access to add, update and delete Todos.
![**Non-Admin View**](non_admin_view.png)

### Add Todo:

User need to submit this form to add a new Todo.
![**Added Todo**](add_todo_form.png)

### After Adding Todo:

List of incomplete Todos. (Todos marked as done will not be listed. Also,
by default Todos are listed in ascending order of Target Date)
![**Added Todo**](todo_home.png)

### After updating Todo:

Updated the description of the first Todo to demonstrate the update
functionality.
![**Update Todo**](updated_todo.png)

### After deleting todo:

Deleted the first Todo to demonstrate the delete functionality.
![**Delete Todo**](deleted_todo.png)

## Admin Controller:

### Homepage for Admin:

Admin user has special "Admin-View" link-button which provide additional
features
like- list all the Todos respective of any users, add/ delete a user
and assign Todo to any existing users.
![**Home Page**](add_todo_for_user.png)

### Add new user:

By submitting this form, Admin can create new a user.
![**Add User**](add_user.png)

### Add Todo for a user as admin:

Admin can add a Todo for any existing users by choosing the username from
the dropdown list.
![**Add User**](admin_add_todo_form.png)

### Delete a user:

To delete a user admin need to select the username from the dropdown and hit
submit.
![**Delete User**](delete_user.png)

