from django.db import models
import requests

from django.core.files import File
from django.core.files.temp import NamedTemporaryFile

# Create your models here.
import base64

class Data(models.Model):
    # timestamp = models.TextField(input_formats=('%Y-%m-%dT%H:%M:%S+0000',))
    time_stamp = models.TextField(null=True)
    latitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    longitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    user_id=models.TextField(null=False, blank=False)
    encoded_image=models.TextField(null=True)
    predicted_class=models.TextField(null=True)
    image_path=models.TextField(null=True)
    crop_type=models.TextField(null=True)

#    classification = models.CharField(max_length=100)
#    language = models.CharField(max_length=100)

def save_image_from_url(model_obj, url):
    r = requests.get(url)

    img_temp = NamedTemporaryFile(delete=True)
    img_temp.write(r.content)
    img_temp.flush()

    model_obj.image.save("image.jpg", File(img_temp), save=True)