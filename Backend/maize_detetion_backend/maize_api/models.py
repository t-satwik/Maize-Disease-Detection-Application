from django.db import models
from django.db.models.fields.related import ForeignKey
import requests

from django.core.files import File
from django.core.files.temp import NamedTemporaryFile

# Create your models here.
import base64
class User(models.Model):
    user_name = models.CharField(blank=False, primary_key=True, max_length=100, default="default_name")
    password = models.CharField(blank=True, null=True, max_length=150)
    email = models.CharField(blank=False, null=True, max_length=100)

class Data(models.Model):
    time_stamp = models.TextField(null=True)
    latitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    longitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    user_name=models.ForeignKey(User, blank=False, on_delete=models.CASCADE, null=True)
    encoded_image=models.TextField(null=True)
    predicted_class=models.TextField(null=True)
    image_path=models.TextField(null=True)
    crop_type=models.TextField(null=True)

class VideoFrame(models.Model):
    time_stamp = models.TextField(null=True)
    startLatitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    startLongitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    endLatitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    endLongitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    user_name=models.ForeignKey(User, blank=False, on_delete=models.CASCADE, null=True)
    encoded_image=models.TextField(null=True)
    predicted_class=models.TextField(null=True)
    image_path=models.TextField(null=True)
    crop_type=models.TextField(null=True)
    probability = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    

class Admin(models.Model):
    user_name = models.CharField(blank=False, primary_key=True, max_length=100, default="default_name")
    password = models.CharField(blank=True, null=True, max_length=150)
    email = models.CharField(blank=False, null=True, max_length=100)