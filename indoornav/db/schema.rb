# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160329105329) do

  create_table "beacons", force: :cascade do |t|
    t.float    "x_coordinate"
    t.float    "y_coordinate"
    t.integer  "map_id",       null: false
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
    t.string   "mac_address",  null: false
  end

  add_index "beacons", ["map_id"], name: "index_beacons_on_map_id"

  create_table "categories", force: :cascade do |t|
    t.string   "name",       null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer  "map_id"
  end

  add_index "categories", ["map_id"], name: "index_categories_on_map_id"

  create_table "destinations", force: :cascade do |t|
    t.string   "name",         null: false
    t.text     "description",  null: false
    t.float    "x_coordinate"
    t.float    "y_coordinate"
    t.integer  "map_id",       null: false
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
    t.integer  "category_id",  null: false
  end

  add_index "destinations", ["category_id"], name: "index_destinations_on_category_id"
  add_index "destinations", ["map_id"], name: "index_destinations_on_map_id"

  create_table "favorites", force: :cascade do |t|
    t.integer  "user_id",        null: false
    t.integer  "destination_id", null: false
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
  end

  add_index "favorites", ["destination_id"], name: "index_favorites_on_destination_id"
  add_index "favorites", ["user_id"], name: "index_favorites_on_user_id"

  create_table "friends", force: :cascade do |t|
    t.integer  "sender_id",   null: false
    t.integer  "receiver_id", null: false
    t.boolean  "status"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  add_index "friends", ["receiver_id"], name: "index_friends_on_receiver_id"
  add_index "friends", ["sender_id"], name: "index_friends_on_sender_id"

  create_table "graphs", force: :cascade do |t|
    t.text     "json",       null: false
    t.integer  "map_id",     null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "graphs", ["map_id"], name: "index_graphs_on_map_id"

  create_table "maps", force: :cascade do |t|
    t.string   "path",       null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "rooms", force: :cascade do |t|
    t.string   "name"
    t.float    "xmin"
    t.float    "ymin"
    t.float    "xmax"
    t.float    "ymax"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer  "map_id"
  end

  add_index "rooms", ["map_id"], name: "index_rooms_on_map_id"

  create_table "transitions", force: :cascade do |t|
    t.float    "x_coordinate"
    t.float    "y_coordinate"
    t.integer  "map_id",       null: false
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
    t.integer  "room_id"
  end

  add_index "transitions", ["map_id"], name: "index_transitions_on_map_id"
  add_index "transitions", ["room_id"], name: "index_transitions_on_room_id"

  create_table "users", force: :cascade do |t|
    t.string   "first_name",         null: false
    t.string   "last_name",          null: false
    t.integer  "day",                null: false
    t.integer  "month",              null: false
    t.integer  "year",               null: false
    t.string   "gender",             null: false
    t.boolean  "en_route"
    t.float    "x_coordinate"
    t.float    "y_coordinate"
    t.datetime "created_at",         null: false
    t.datetime "updated_at",         null: false
    t.string   "username",           null: false
    t.string   "encrypted_password", null: false
    t.string   "token"
    t.integer  "room_id"
  end

  add_index "users", ["room_id"], name: "index_users_on_room_id"
  add_index "users", ["username"], name: "index_users_on_username", unique: true

  create_table "walls", force: :cascade do |t|
    t.string   "name"
    t.float    "xmin"
    t.float    "ymin"
    t.float    "xmax"
    t.float    "ymax"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

end
