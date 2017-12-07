class CreateFriends < ActiveRecord::Migration
  def change
    create_table :friends do |t|
      t.references :sender, index: true
      t.references :receiver, index: true
      t.boolean :status

      t.timestamps null: false
    end
    add_foreign_key :friends, :senders
    add_foreign_key :friends, :receivers
  end
end
