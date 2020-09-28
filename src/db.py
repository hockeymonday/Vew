from flask_sqlalchemy import SQLAlchemy
db = SQLAlchemy()

#Classes for Content Management
class AllImages (db.Model):
    """Table containing all image data"""
    __tablename__ = "pics"
    id = db.Column(db.Integer, primary_key=True)
    uncomp = db.Column(db.String, nullable=False)
    comp = db.Column(db.String, nullable=False)
    title = db.Column(db.String, nullable=True)
    contributor = db.Column(db.String, nullable=True)
    resolution = db.Column(db.Integer, nullable = True)

    def serialize_images(self):
        "Returns the information in the table"
        return {
            "id": self.id,
            "uncompressed_url": self.uncomp,
            "compressed_url": self.comp,
            "title": self.title,
            "contributor": self.contributor,
            "resolution": self.resolution
        } 


#Class for Security
class Password (db.Model):
    """Table containing usernames and passwords of administrators"""
    __tablename__ = "login_info"
    id = db.Column(db.Integer, primary_key=True)
    user = db.Column(db.Integer, nullable = False)
    pass_hash = db.Column(db.Integer, nullable = False)

    def get_hash(self):
         return self.pass_hash


