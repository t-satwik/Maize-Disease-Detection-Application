from django.db import models

# Create your models here.

class Data(models.Model):
    # timestamp = models.TextField(input_formats=('%Y-%m-%dT%H:%M:%S+0000',))
    timestamp = models.TextField()
    latitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    longitude = models.DecimalField(max_digits=22, decimal_places=16, blank=True, null=True)
    user_id=models.IntegerField(null=False, blank=False)
    # image=models.ImageField(upload_to='uploads/', height_field=224, width_field=224, max_length=100)
#    classification = models.CharField(max_length=100)
#    language = models.CharField(max_length=100)

