class ModifyUsers < ActiveRecord::Migration
  def change
    add_column :users, :username, :string
    add_column :users, :encrypted_password, :string
    add_column :users, :token, :string

    change_column_null :users, :username, false
    change_column_null :users, :encrypted_password, false

    add_index :users, :username, unique: true
  end
end
