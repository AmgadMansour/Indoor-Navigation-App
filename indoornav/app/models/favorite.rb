class Favorite < ActiveRecord::Base
  # Fields

  # Validations
  validates :user_id, :destination_id, presence: true

  # Relations
  belongs_to :user
  belongs_to :destination

  # Callbacks

  # Class Methods

  # Methods
  
end
