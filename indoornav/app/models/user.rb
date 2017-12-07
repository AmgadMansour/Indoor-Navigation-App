class User < ActiveRecord::Base
  # Fields
  attr_accessor :password

  # Validations
  validates :first_name, :last_name, :day, :month, :year, :gender, :username, presence: true
  validates :username, uniqueness: true
  validates :password, presence: true, on: :create

  # Relations
  has_many :favorites, dependent: :destroy
  has_many :destinations, through: :favorites

  has_many :friends, dependent: :destroy
  has_many :users, through: :friends

  # Callbacks
  before_save :encrypt_password, if: -> { self.password.present? }
  before_create -> { self.token = SecureRandom.hex }, unless: :token?

  # Class Methods
  def self.authenticate(username, password)
    user = find_by(username: username)
    if user.present? && user.valid_password?(password)
      user
    else
      User.new.tap do |user|
        user.errors.add :base, 'Invalid username or password'
      end
    end
  end

  # Methods
  def valid_password?(password)
    self.encrypted_password == Digest::MD5.hexdigest(password)
  end

  def encrypt_password
    self.encrypted_password = Digest::MD5.hexdigest(self.password)
  end
end
