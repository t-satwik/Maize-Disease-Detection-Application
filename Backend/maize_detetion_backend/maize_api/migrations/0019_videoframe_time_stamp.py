# Generated by Django 3.2.8 on 2021-11-03 20:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0018_videoframe'),
    ]

    operations = [
        migrations.AddField(
            model_name='videoframe',
            name='time_stamp',
            field=models.TextField(null=True),
        ),
    ]
