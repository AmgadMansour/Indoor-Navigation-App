class Friend < ActiveRecord::Base
  # Fields

  # Validations
  validates :sender_id, :receiver_id, presnece: true

  # Relations
  belongs_to :user
  belongs_to :user

  # Callbacks
  # Class Methods
  # Methods
end
